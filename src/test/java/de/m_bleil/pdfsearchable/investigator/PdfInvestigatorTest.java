package de.m_bleil.pdfsearchable.investigator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PdfInvestigatorTest {

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
	public void testThatInvetsigationMatchesAllFiles() throws IOException {
		long numberOfFiles = Files.walk(Paths.get("pdfs")).count();
		int numberOfLicenceFiles = 1;
		long mumberOfRootDirectory = 1;
		PdfInvestigator investigator = new PdfInvestigator();

		List<PdfInfo> result = investigator.investigatePath("pdfs");

		assertThat(result.size())
				.isEqualTo(numberOfFiles - mumberOfRootDirectory - numberOfLicenceFiles);
	}
}
