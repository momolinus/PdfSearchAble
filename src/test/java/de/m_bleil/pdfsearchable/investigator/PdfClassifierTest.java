package de.m_bleil.pdfsearchable.investigator;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PdfClassifierTest {

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
