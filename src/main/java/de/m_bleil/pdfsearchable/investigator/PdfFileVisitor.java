package de.m_bleil.pdfsearchable.investigator;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;

import org.tinylog.Logger;

public class PdfFileVisitor extends SimpleFileVisitor<Path> {

	private List<Path> pdfFiles;

	public PdfFileVisitor(List<Path> pdfFiles) {
		this.pdfFiles = pdfFiles;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		pdfFiles.add(file);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		if (Objects.nonNull(exc)) {
			Logger.warn(exc, "error with file {}", file);
		}
		return FileVisitResult.CONTINUE;
	}
}