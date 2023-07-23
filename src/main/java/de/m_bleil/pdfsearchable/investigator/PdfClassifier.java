package de.m_bleil.pdfsearchable.investigator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PdfClassifier {

	public int classify(PdfInfo emptyText) {

		if (emptyText.content() == null)
			return 0;
		if (emptyText.content().equals(""))
			return 0;

		if (emptyText.content().length() > 0) {
			String[] characters =
					emptyText
							.content()
							.lines()
							.collect(Collectors.joining(" "))
							.split("");

			List<String> charactersNotEmpty =
					Arrays
							.stream(characters)
							.filter(z -> !z.equals(" "))
							.collect(Collectors.toList());

			return charactersNotEmpty.size() > 0 ? 100 : 0;
		}

		return 0;
	}

}
