package de.m_bleil.pdfsearchable.application.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PdfClassifier {

	public boolean classify(String text) {
		if (text == null)
			return false;
		if (text.equals(""))
			return false;

		String[] characters =
				text
						.lines()
						.collect(Collectors.joining(" "))
						.split("");

		List<String> charactersNotEmpty =
				Arrays
						.stream(characters)
						.filter(z -> !z.equals(" "))
						.filter(z -> z.length() > 0)
						.collect(Collectors.toList());

		return charactersNotEmpty.size() > 0;
	}
}
