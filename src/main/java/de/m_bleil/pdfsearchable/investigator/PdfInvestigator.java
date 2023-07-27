package de.m_bleil.pdfsearchable.investigator;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.tinylog.Logger;

public class PdfInvestigator {

	public static final String PDF_FILE_EXTENSION = ".pdf";

	private static class PdfFileVisitor implements FileVisitor<Path> {

		private List<Path> pdfFiles;

		public PdfFileVisitor(List<Path> pdfFiles) {
			this.pdfFiles = pdfFiles;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			pdfFiles.add(file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			if (Objects.nonNull(exc)) {
				Logger.warn(exc, "error with file {}", file);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			if (Objects.nonNull(exc)) {
				Logger.warn(exc);
			}
			return FileVisitResult.CONTINUE;
		}
	}

	private ArrayList<PdfInfo> buildResult(Map<Path, PDDocument> pdfDocuments,
			CompletionService<PdfInfo> completionService)
			throws InterruptedException, ExecutionException {

		var results = new ArrayList<PdfInfo>();

		for (int i = 0; i < pdfDocuments.size(); i++) {
			Future<PdfInfo> result;

			result = completionService.take();
			results.add(result.get());
		}

		return results;
	}

	private Map<Path, PDDocument> fetchPDDocuments(List<Path> pdfFiles) {
		return pdfFiles
				.stream()
				.filter(f -> f.toString().endsWith(PDF_FILE_EXTENSION))
				.collect(Collectors.toMap(f -> f, f -> {
					PDDocument pdD = null;
					try {
						pdD = PDDocument.load(f.toFile());
						return pdD;
					}
					catch (IOException e) {
						Logger.warn(e);
						return null;
					}
				}));
	}

	public List<PdfInfo> investigatePath(Path root) {
		List<Path> pdfFiles = null;
		Map<Path, PDDocument> pdfDocuments;
		ExecutorService pool = null;
		CompletionService<PdfInfo> completionService;
		ArrayList<PdfInfo> results;

		try {
//			pdfFiles = Files
//					.walk(root, FileVisitOption.FOLLOW_LINKS)
//					.collect(Collectors.toList());

			pdfFiles = new ArrayList<Path>();
			Files.walkFileTree(root, new PdfFileVisitor(pdfFiles));

			pdfDocuments = fetchPDDocuments(pdfFiles);

			pool = Executors.newCachedThreadPool();
			completionService = new ExecutorCompletionService<>(pool);

			long start = System.nanoTime();

			pdfDocuments.forEach((p, d) -> completionService.submit(new ExtractText(p, d)));

			results = buildResult(pdfDocuments, completionService);

			pdfDocuments.forEach((p, d) -> IOUtils.closeQuietly(d));

			Logger.trace("Duration: {}", (System.nanoTime() - start));

		}
		catch (IOException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (pool != null)
				pool.shutdown();
		}

		return results;
	}

	public List<PdfInfo> investigatePath(String string) {
		return investigatePath(Paths.get(string));
	}
}