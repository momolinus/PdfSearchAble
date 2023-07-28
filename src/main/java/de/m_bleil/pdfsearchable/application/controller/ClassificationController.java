package de.m_bleil.pdfsearchable.application.controller;

import de.m_bleil.pdfsearchable.application.model.ClassificationModel;

public class ClassificationController {

	private ClassificationModel classificationModel;

	public ClassificationController(ClassificationModel classificationModel) {
		this.classificationModel = classificationModel;
	}

	public void startButtonEvent(String path) {
		classificationModel.updatePdfs(path);
	}
}
