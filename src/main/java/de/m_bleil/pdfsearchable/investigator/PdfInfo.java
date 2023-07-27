package de.m_bleil.pdfsearchable.investigator;

import java.nio.file.Path;

public record PdfInfo(Path path, String content, Boolean searchable) {

	/**
	 * note: Path and String are immutable
	 */
}
