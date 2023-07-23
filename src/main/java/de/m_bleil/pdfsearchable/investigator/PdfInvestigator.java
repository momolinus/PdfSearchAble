package de.m_bleil.pdfsearchable.investigator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.tinylog.Logger;

public class PdfInvestigator {

	public static final String PDF_FILE_EXTENSION = ".pdf";

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
					try {
						return PDDocument.load(f.toFile());
					} catch (IOException e) {
						throw new RuntimeException(e);
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
			pdfFiles = Files.walk(root).collect(Collectors.toList());

			pdfDocuments = fetchPDDocuments(pdfFiles);

			pool = Executors.newCachedThreadPool();
			completionService = new ExecutorCompletionService<>(pool);

			long start = System.nanoTime();

			pdfDocuments.forEach((p, d) -> completionService.submit(new ExtractText(p, d)));

			results = buildResult(pdfDocuments, completionService);

			Logger.trace("Duration: {}", (System.nanoTime() - start));

		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		} finally {
			if (pool != null)
				pool.shutdown();
		}

		return results;
	}

	public List<PdfInfo> investigatePath(String string) {
		return investigatePath(Paths.get(string));
	}
}