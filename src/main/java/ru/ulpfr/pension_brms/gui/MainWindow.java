package ru.ulpfr.pension_brms.gui;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;

public class MainWindow extends JFrame {

	/**
	 * Главное окно приложения
	 */
	private static final long serialVersionUID = 1L;
	private static MainWindow instance;
	
	public XMLPanel topPanel;
	public OutputPanel outputPanel;
	private JSplitPane splitLayout;

	public MainWindow() throws HeadlessException {
		super();
		setLayout(new FlowLayout());
		setTitle("Calculation of pension");	
		initPanels();
		initListeners();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //чтобы процесс приложения завершался после закрытия окна
		setSize(500, 700); //размер окна по дефолту
		setVisible(true); // отображаем окно
	}
	
	private void initPanels() {
		topPanel = new XMLPanel();
		outputPanel = new OutputPanel();
		splitLayout = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, outputPanel);
	}
	public void showPanels() {
		getContentPane().add(splitLayout);
	}
	
	public void hidePanels() {
		getContentPane().remove(splitLayout);
	}
	
	public void output(String msg) {
		outputPanel.appendMsg(msg, MESSAGE_TYPE.RULES);
	}
	
	public void output(String msg, MESSAGE_TYPE type) {
		outputPanel.appendMsg(msg, type);
	}
	
	public void output(List<String> list, MESSAGE_TYPE type) {
		for (String error : list) {
			output(error, type);
		}
	}
	
	
	public static synchronized MainWindow getInstance() {
		if (instance == null)
			instance = new MainWindow();
		return instance;
	}
	
	private void initListeners() {
		this.addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 System.out.println("Window Closed");
	                System.exit(0);
	            }
			 public void windowOpened(WindowEvent e) {
	                System.out.println("Window Opened");
	            }
		});
	}
}
