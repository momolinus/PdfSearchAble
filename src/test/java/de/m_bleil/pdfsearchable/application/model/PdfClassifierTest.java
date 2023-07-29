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
	public void testATextCorrectClassified1() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("Hello World");

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testATextCorrectClassified2() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("Hello\n\rWorld\n\rBerlin");

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testEmptyTextCorrectClassified1() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("");

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testEmptyTextCorrectClassified2() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("   \n   ");

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testNewLinesCorrectClassified1() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("\nz\r");

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testNewLinesCorrectClassified2() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("Lorem\nIpsum");

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testNullTextCorrectClassified() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify(null);

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testOnlyNewLinesCorrectClassified1() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("\n\r");

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testOnlyNewLinesCorrectClassified2() {
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify("\n");

		assertThat(searchAbel, is(false));
	}
}
