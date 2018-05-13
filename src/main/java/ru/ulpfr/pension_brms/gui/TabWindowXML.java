package ru.ulpfr.pension_brms.gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class TabWindowXML extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private XMLPanel topPanel;
	private OutputPanel outputPanel;
	private JSplitPane splitLayout;

	public TabWindowXML() {
		setLayout(new FlowLayout());
		initPanels();
		setSize(500, 600); //размер окна по дефолту
		setVisible(true); // отображаем окно
		showPanels();
	}
	
	private void initPanels() {
		topPanel = new XMLPanel();
		outputPanel = new OutputPanel();
		splitLayout = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, outputPanel);
	}
	public void showPanels() {
		add(splitLayout);
	}
	
	public void hidePanels() {
		remove(splitLayout);
	}
	
	public OutputPanel getOutput() {
		return outputPanel;
	}


}
