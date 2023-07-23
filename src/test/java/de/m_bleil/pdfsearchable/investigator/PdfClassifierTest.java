package de.m_bleil.pdfsearchable.investigator;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PdfClassifierTest {

	@Test
	public void testEmptyTextCorrectClassified() {
		PdfInfo emptyText = new PdfInfo(null, "");
		PdfClassifier classifier = new PdfClassifier();

		int searchAbel = classifier.classify(emptyText);

		assertThat(searchAbel, is(0));
	}

	@Test
	public void testNullTextCorrectClassified() {
		PdfInfo nullText = new PdfInfo(null, null);
		PdfClassifier classifier = new PdfClassifier();

		int searchAbel = classifier.classify(nullText);

		assertThat(searchAbel, is(0));
	}

	@Test
	public void testATextCorrectClassified() {
		PdfInfo nullText = new PdfInfo(null, "Hello World");
		PdfClassifier classifier = new PdfClassifier();

		int searchAbel = classifier.classify(nullText);

		assertThat(searchAbel, is(100));
	}
}
