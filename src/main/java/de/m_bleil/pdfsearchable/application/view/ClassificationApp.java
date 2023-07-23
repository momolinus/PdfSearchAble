package de.m_bleil.pdfsearchable.application.view;

import javax.swing.JFrame;

import de.m_bleil.pdfsearchable.application.controller.ClassificationController;

public class ClassificationApp extends JFrame {

	private ClassificationController c;

	public ClassificationApp() {
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		ClassificationApp application = new ClassificationApp();
		application.setVisible(true);
	}
}
