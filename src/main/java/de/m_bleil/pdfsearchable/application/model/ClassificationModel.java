package de.m_bleil.pdfsearchable.application.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ClassificationModel implements PropertyChangeListener {

	private List<ClassificationModelListener> listener = new ArrayList<>();

	public void updatePdfs(String path) {
		PdfClassificationUpdater classifier = new PdfClassificationUpdater(path);

		classifier.addPropertyChangeListener(this);
		classifier.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (PdfClassificationUpdater.PDF_CLASSIFIED.equals(event.getPropertyName())) {

			PdfInfo pdfInfo = (PdfInfo) event.getNewValue();
			listener.forEach(l -> l.newPdfInfoClassification(pdfInfo));
		}
	}

	public void addListener(ClassificationModelListener l) {
		listener.add(l);
	}
}