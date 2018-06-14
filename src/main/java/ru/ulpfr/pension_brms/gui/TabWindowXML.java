package ru.ulpfr.pension_brms.gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ru.ulpfr.pension_brms.gui.MainWindow.TABS;
import ru.ulpfr.pension_brms.listeners.GlobalListener;
import ru.ulpfr.pension_brms.managers.DroolsManager;
import ru.ulpfr.pension_brms.managers.DroolsManager.RULES_TYPES;
import ru.ulpfr.pension_brms.managers.InputDataManager.READER_STATUS;

public class TabWindowXML extends JPanel implements GlobalListener {
	
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
	
	public Boolean getShowExecutedRules() {
		return topPanel.showExecutedRules();
	}

	@Override
	public void windowReady() {
		System.out.println("You may load xml file");
		topPanel.setEnable(true);
		topPanel.updateStatus(READER_STATUS.INIT);
	}
	
	@Override
	public void windowNotReady() {
		System.out.println("You can't load xml file");
		topPanel.setEnable(false);
	}
	
	@Override
	public void allowDroolsInit(RULES_TYPES mode) {
		System.out.println("allowDroolsInit mode ="+mode);
		DroolsManager.init(mode).execute(TABS.XML);
	}
}
