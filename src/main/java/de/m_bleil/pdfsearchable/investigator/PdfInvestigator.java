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

public class PdfInvestigator {

	public static final String PDF_FILE_EXTENSION = ".pdf";

	public List<PdfInfo> investigatePath(Path root) {
		List<Path> pdfFiles = null;
		Map<Path, PDDocument> pdfDocuments;
		ExecutorService pool = null;
		CompletionService<PdfInfo> completionService;
		var results = new ArrayList<PdfInfo>();

		try {
			pdfFiles = Files.walk(root).collect(Collectors.toList());

			pdfDocuments = pdfFiles
					.stream()
					.filter(f -> f.toString().endsWith(PDF_FILE_EXTENSION))
					.collect(Collectors.toMap(f -> f, f -> {
						try {
							return PDDocument.load(f.toFile());
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}));

			pool = Executors.newCachedThreadPool();
			completionService = new ExecutorCompletionService<>(pool);

			long start = System.nanoTime();

			pdfDocuments.forEach((path, doc) -> {
				completionService.submit(new ExtractText(path, doc));
			});

			for (int i = 0; i < pdfDocuments.size(); i++) {
				Future<PdfInfo> result;

				result = completionService.take();
				results.add(result.get());
			}

			System.out.println("Duration: " + (System.nanoTime() - start));
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
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