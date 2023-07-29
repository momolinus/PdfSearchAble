package de.m_bleil.pdfsearchable.application.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PdfClassifierTest {

	/*
	 * Write good tests:
	 * Arrange
	 * Act
	 * Assert
	 *
	 * see: Michael Tamm, JUnit Profiwissen (1. Auflage 2013), Kapitel 7.5 Der AAA
	 * Stil
	 */

	@Test
	public void testATextCorrectClassified() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("Hello World");

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testEmptyTextCorrectClassified() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("");

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testNewLinesCorrectClassified() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("\nz\r");

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testNullTextCorrectClassified() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify(null);

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testOnlyNewLinesCorrectClassified() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("\n\r");

		assertThat(searchAbel, is(false));
	}
}
