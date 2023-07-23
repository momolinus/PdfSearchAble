package de.m_bleil.pdfsearchable.inspectiontest;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StringInspection {

	@Test
	public void testLengthOfEmptyString() {
		String emptyString = new String("");

		assertThat(emptyString, hasLength(0));
	}
}
