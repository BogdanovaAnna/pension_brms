package ru.ulpfr.pension_brms.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ru.ulpfr.pension_brms.gui.MainWindow.TABS;
import ru.ulpfr.pension_brms.managers.DroolsManager;

public class TabWindowRuntime extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton startBtn;
	
	public TabWindowRuntime() {
		startBtn = new JButton("Запуск интервью");
		startBtn.setSize(new Dimension(100, 30));
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DroolsManager.getInstance().execute(TABS.RUNTIME);
			}
		});	
		add(startBtn);
	}


}
