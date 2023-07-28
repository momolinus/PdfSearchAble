package de.m_bleil.pdfsearchable.application.model;

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

import javax.swing.SwingWorker;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.tinylog.Logger;

import de.m_bleil.pdfsearchable.investigator.ExtractText;
import de.m_bleil.pdfsearchable.investigator.PdfClassifier;
import de.m_bleil.pdfsearchable.investigator.PdfFileVisitor;
import de.m_bleil.pdfsearchable.investigator.PdfInfo;

public class PdfClassificationUpdater extends SwingWorker<List<PdfInfo>, PdfInfo> {

	public static final String PDF_FILE_EXTENSION = ".pdf";
	public static final String PDF_CLASSIFIED = "pdf_classified";

	private String path;
	private PdfClassifier classifier;

	public PdfClassificationUpdater(String path) {
		this.path = path;
		classifier = new PdfClassifier();
	}

	private ArrayList<PdfInfo> buildResult(Map<Path, PDDocument> pdfDocuments,
			CompletionService<PdfInfo> completionService)
			throws InterruptedException, ExecutionException {

		var results = new ArrayList<PdfInfo>();

		for (int i = 0; i < pdfDocuments.size(); i++) {
			Future<PdfInfo> result;

			result = completionService.take();
			PdfInfo pdfInfo = result.get();
			results.add(pdfInfo);
			publish(pdfInfo);

			firePropertyChange(PDF_CLASSIFIED, null, pdfInfo);
		}

		return results;
	}

	private Map<Path, PDDocument> fetchPDDocuments(List<Path> pdfFiles) {
		return pdfFiles
				.stream()
				.filter(f -> f.toString().endsWith(PDF_FILE_EXTENSION))
				.collect(Collectors.toMap(f -> f, f -> {
					try {
						var pdD = PDDocument.load(f.toFile());
						return pdD;
					}
					catch (IOException e) {
						Logger.warn(e);
						var pdD = new PDDocument();
						pdD.addPage(new PDPage());
						return pdD;
					}
				}));
	}

	@Override
	protected List<PdfInfo> doInBackground() throws Exception {
		List<Path> pdfFiles = null;
		Map<Path, PDDocument> pdfDocuments;
		ExecutorService pool = null;
		CompletionService<PdfInfo> completionService;
		ArrayList<PdfInfo> results;

		try {

			pdfFiles = new ArrayList<Path>();
			Files.walkFileTree(Paths.get(path), new PdfFileVisitor(pdfFiles));

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

	@Override
	protected void done() {
		// TODO Auto-generated method stub
		super.done();
	}

	@Override
	protected void process(List<PdfInfo> chunks) {
		// TODO Auto-generated method stub
		super.process(chunks);
	}
}