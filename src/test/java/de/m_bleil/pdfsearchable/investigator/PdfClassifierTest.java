package de.m_bleil.pdfsearchable.investigator;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PdfClassifierTest {

	@Test
	public void testATextCorrectClassified() {
		PdfInfo text = new PdfInfo(null, "Hello World");
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify(text);

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testEmptyTextCorrectClassified() {
		PdfInfo emptyText = new PdfInfo(null, "");
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify(emptyText);

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testNewLinesCorrectClassified() {
		PdfInfo newLineText = new PdfInfo(null, "\nz\r");
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify(newLineText);

		assertThat(searchAbel, is(true));
	}

	@Test
	public void testNullTextCorrectClassified() {
		PdfInfo nullText = new PdfInfo(null, null);
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify(nullText);

		assertThat(searchAbel, is(false));
	}

	@Test
	public void testOnlyNewLinesCorrectClassified() {
		PdfInfo newLineText = new PdfInfo(null, "\n\r");
		PdfClassifier classifier = new PdfClassifier();

		boolean searchAbel = classifier.classify(newLineText);

		assertThat(searchAbel, is(false));
	}
}
