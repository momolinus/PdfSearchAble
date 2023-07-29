package de.m_bleil.pdfsearchable.application.model;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.tinylog.Logger;

public class ExtractText implements Callable<PdfInfo> {

	private PDDocument document;

	private Path path;

	private PdfClassifier classifier;

	/**
	 * note: Path is immutable, PDDocument is mutable
	 */

	public ExtractText(Path path, PDDocument document) {
		this.path = path;
		this.document = document;
		this.classifier = new PdfClassifier();
	}

	@Override
	public PdfInfo call() throws Exception {
		Logger.trace("Thread: {}", Thread.currentThread().getName());

		PDFTextStripper pdfStripper = new PDFTextStripper();
		String content = pdfStripper.getText(this.document);

		return new PdfInfo(this.path, content, classifier.classify(content));
	}
}