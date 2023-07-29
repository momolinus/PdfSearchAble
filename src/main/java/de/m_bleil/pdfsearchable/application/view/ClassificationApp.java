package de.m_bleil.pdfsearchable.application.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.m_bleil.pdfsearchable.application.controller.ClassificationController;
import de.m_bleil.pdfsearchable.application.model.ClassificationModel;
import de.m_bleil.pdfsearchable.application.model.ClassificationModelListener;
import de.m_bleil.pdfsearchable.application.model.PdfClassifier;
import de.m_bleil.pdfsearchable.application.model.PdfInfo;

public class ClassificationApp extends JFrame implements ClassificationModelListener {

	private JFileChooser rootChooser;
	private JTextField rootPathTextField;
	private JTextArea resultTextArea;

	private ClassificationModel classificationModel;
	private ClassificationController classificationController;

	public ClassificationApp() {
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		this.setLayout(new BorderLayout(20, 20));

		this.classificationModel = new ClassificationModel();
		this.classificationModel.addListener(this);
		this.classificationController = new ClassificationController(this.classificationModel);

		initGui();
	}

	private void onOpenRoot(ActionEvent event) {
		int answer = rootChooser.showOpenDialog(this);

		if (answer == JFileChooser.APPROVE_OPTION) {
			rootPathTextField.setText(rootChooser.getSelectedFile().toString());
		}
	}

	private void initGui() {
		initRootChooserPanel();

		resultTextArea = new JTextArea();
		JScrollPane resultTextAreasScrollPane = new JScrollPane(resultTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(resultTextAreasScrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		JButton startButton = new JButton("Start");
		panel.add(startButton);
		this.add(panel, BorderLayout.SOUTH);
		startButton.addActionListener(
				e -> classificationController
						.startButtonEvent(rootPathTextField.getText()));
	}

	private void initRootChooserPanel() {
		JButton openRoot = new JButton("Verzeichnis o. Datei wählen");
		rootPathTextField = new JTextField();
		JPanel rootSelectionPanel = new JPanel();
		BoxLayout layout = new BoxLayout(rootSelectionPanel, BoxLayout.X_AXIS);
		rootSelectionPanel.setLayout(layout);

		rootSelectionPanel.add(Box.createHorizontalStrut(10));
		rootSelectionPanel.add(new JLabel("Startverzeichnis oder Datei"));
		rootSelectionPanel.add(Box.createHorizontalStrut(10));
		rootSelectionPanel.add(rootPathTextField);
		rootSelectionPanel.add(Box.createHorizontalStrut(10));
		rootSelectionPanel.add(openRoot);
		rootSelectionPanel.add(Box.createHorizontalStrut(10));

		this.add(rootSelectionPanel, BorderLayout.NORTH);

		rootChooser = new JFileChooser("C:\\Users\\Marcus\\git\\PdfSearchAble\\pdfs");
		rootChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		rootChooser.setMultiSelectionEnabled(false);
		rootChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		rootChooser.setSelectedFile(new File("C:\\Users\\Marcus\\git\\PdfSearchAble\\pdfs"));

		openRoot.addActionListener(this::onOpenRoot);
	}

	public static void main(String[] args) {
		ClassificationApp application = new ClassificationApp();
		application.setVisible(true);
	}

	@Override
	public void newPdfInfoClassification(PdfInfo pdfInfo) {
		PdfClassifier classifier = new PdfClassifier();

		resultTextArea.setText(resultTextArea.getText()
				+ (classifier.classify(pdfInfo.content()) ? "durchsuchbar:"
						: "nicht durchsuchbar:")
				+ "\t"
				+ pdfInfo.path()
				+ "\n");
	}
}
