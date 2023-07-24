package de.m_bleil.pdfsearchable.application.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClassificationApp extends JFrame {

	public ClassificationApp() {
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		this.setLayout(new BorderLayout(20, 20));
		init();
	}

	private void onOpenRoot(ActionEvent event) {

	}

	private void init() {
		JButton openRoot = new JButton("Verzeichnis o. Datei wählen");
		JTextField rootPathTextField = new JTextField();
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

		openRoot.addActionListener(this::onOpenRoot);
	}

	public static void main(String[] args) {
		ClassificationApp application = new ClassificationApp();
		application.setVisible(true);
	}
}
