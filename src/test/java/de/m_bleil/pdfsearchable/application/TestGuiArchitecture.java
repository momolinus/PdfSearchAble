package de.m_bleil.pdfsearchable.application;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public class TestGuiArchitecture {

	@Test
	public void testMvcLayer() {
		JavaClasses importedClasses = new ClassFileImporter()
				.importPackages("de.m_bleil.pdfsearchable.application");

		var rule = layeredArchitecture()
				.consideringAllDependencies()
				.layer("Controller").definedBy("..controller..")
				.layer("Model").definedBy("..model..")
				.layer("View").definedBy("..view..")

				.whereLayer("Controller").mayOnlyBeAccessedByLayers("View")
				.whereLayer("Model").mayOnlyBeAccessedByLayers("Controller", "View")
				.whereLayer("View").mayOnlyBeAccessedByLayers("Controller");

		rule.check(importedClasses);
	}

}
