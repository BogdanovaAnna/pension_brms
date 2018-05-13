package ru.ulpfr.pension_brms.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;

public class MainWindow extends JFrame {

	/**
	 * Главное окно приложения
	 */
	private static final long serialVersionUID = 1L;
	private static MainWindow instance;
	
	private JTabbedPane jtp;
	private TabWindowXML xmlPanel;
	private TabWindowRuntime rtPanel;
	
	public enum TABS {
		XML,
		RUNTIME
	}

	public MainWindow() throws HeadlessException {
		super();
		setLayout(new FlowLayout());
		setTitle("Calculation of pension");	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //чтобы процесс приложения завершался после закрытия окна
		setSize(500, 700); //размер окна по дефолту
		initListeners();
		createTabs();
		setVisible(true); // отображаем окно
	}
	
	private  void createTabs() {
		UIManager.put("TabbedPane.selected", new Color(45, 89, 134));
		xmlPanel = new TabWindowXML();
		rtPanel = new TabWindowRuntime();

		jtp = new JTabbedPane();
		jtp.setBackground(new Color(191, 191, 191));
		jtp.setBorder(null);
		jtp.add(xmlPanel);
		jtp.add( rtPanel);;
		
		jtp.setTabComponentAt(0, getTitleLabel("XML"));
		jtp.setTabComponentAt(1, getTitleLabel("Runtime"));
		 
		getContentPane().add(jtp);
	}
	
	private JLabel getTitleLabel(String msg) {
		JLabel title = new JLabel(msg, SwingConstants.CENTER);
		title.setPreferredSize(new Dimension(100, 25));
		title.setForeground(new Color(255, 255, 255));
		return title;
	}
	
	public void setActiveTab(TABS tab) {
		switch (tab) {
			case XML:
				jtp.setSelectedIndex(0);
				break;
			case RUNTIME:
				jtp.setSelectedIndex(1);
				break;
		}
		
	}
	
	
	
	public static void output(String msg) {
		if(MainWindow.getInstance().jtp.getSelectedIndex() == 0)
			MainWindow.getInstance().xmlPanel.getOutput().appendMsg(msg, MESSAGE_TYPE.RULES);
	}
	
	public static void output(String msg, MESSAGE_TYPE type) {
		if(MainWindow.getInstance().jtp.getSelectedIndex() == 0)
			MainWindow.getInstance().xmlPanel.getOutput().appendMsg(msg, type);
	}
	
	public static void output(List<String> list, MESSAGE_TYPE type) {
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
