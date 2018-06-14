package ru.ulpfr.pension_brms.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;
import ru.ulpfr.pension_brms.managers.DroolsManager.RULES_TYPES;
import ru.ulpfr.pension_brms.managers.ApplicationManager;
import ru.ulpfr.pension_brms.managers.InputDataManager;
import ru.ulpfr.pension_brms.managers.InputDataManager.READER_STATUS;

public class XMLPanel extends JPanel {
	
	/**
	 * Панель для загрузки XML-файлов
	 */
	private static final long serialVersionUID = -2482425422212218523L;
	private JFileChooser fch;
	private JButton btn;
	private JTextField txtField;
	private JCheckBox showExecutedRules, dslMode;
	private Box topBox, bottomBox;
	private File selectedFile;

	public XMLPanel() {
		setBorder(BorderFactory.createTitledBorder("Выберете XML-файл для обработки"));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(460, 100));
		initFileChooser();
		initPathText();
		initCheckBoxOptions();
		initFileButton();
		initBoxes();
		addComponents();
	}
	
	private void initFileChooser() {
		UIManager.put("FileChooser.cancelButtonText", "Отмена");
		UIManager.put("FileChooser.openButtonText", "Выбрать");
		UIManager.put("FileChooser.lookInLabelText", "Директория");
		UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
		UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов");

		fch = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fch.setDialogTitle("Выберите файл: ");
		fch.setMultiSelectionEnabled(false);
		fch.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fch.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Xml файлы (*.xml)", "xml");
		fch.addChoosableFileFilter(filter);
		
	}
	
	private void initFileButton() {
		btn = new JButton("Загрузить файл");
		btn.setSize(new Dimension(100, 25));
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btn.setEnabled(false);
				int returnValue = fch.showOpenDialog(btn);
				READER_STATUS status = READER_STATUS.INVALID_DATA;
				RULES_TYPES mode = dslMode.isSelected() ? RULES_TYPES.DSL : RULES_TYPES.DRL;
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = fch.getSelectedFile();
					txtField.setText(selectedFile.getAbsolutePath());
					status = InputDataManager.getInstance().parseXmlFile(selectedFile);
					updateStatus(status);
				}
				if (status == READER_STATUS.SUCCESS) {
					Thread t = new Thread(new Runnable() {
                        public void run() {
                        	ApplicationManager.getInstance().startDrools(mode);
                        	 btn.setEnabled(true);
                        }
                     });
					t.start();
				} else  
					btn.setEnabled(true);
			}
		});	
	}
	
	private void initCheckBoxOptions() {
		showExecutedRules = new JCheckBox("Показать выполненные правила");
		showExecutedRules.setSelected(true);
		dslMode = new JCheckBox("DSL");
		dslMode.setSelected(false); // если false - правила будут читаться из DRL файлов
	}
	
	private void initPathText() {
		txtField = new JTextField("Путь к xml-файлу отсутствует");
		txtField.setColumns(26);
		txtField.setEditable(false);
	}
	
	private void initBoxes() {
		topBox = Box.createHorizontalBox();
		bottomBox = Box.createHorizontalBox();
		topBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		bottomBox.setBorder(BorderFactory.createEmptyBorder(5, 150, 0, 0));
		topBox.add(btn);
		topBox.add(txtField);
		bottomBox.add(dslMode);
		bottomBox.add(Box.createRigidArea(new Dimension(15,0)));
		bottomBox.add(showExecutedRules);
	}
	
	public void addComponents() {
		this.add(topBox);
		this.add(bottomBox);
	}
	
	public void updateStatus(READER_STATUS status, Object data) {
		updateStatus(status);
	}
	
	public void updateStatus(READER_STATUS status) {
		String str = "";
		switch (status) {
		case ERROR_SINTAX:
			str = "не выполнено. Присутствуют ошибки синтаксиса в файле";
			break;
		case INVALID_DATA:
			str = "не выполнено. Данные в xml не прошли валидацию";
			break;
		case INVALID_TAG_STRUCTURE:
			str = "не выполнено. В структуре xml нет блока clients/client";
			break;
		case SUCCESS:
			str = "Файл " + selectedFile.getName() +" успешно загружен!";
			break;
		case INIT:
			str = "Выберете XML-файл...";
			break;
		default:
			str = "Непонятный апдейт статуса";
			break;
		}
		MainWindow.output(str,MESSAGE_TYPE.INFO);
	}
	
	public void reset() {
		updateStatus(READER_STATUS.INIT);
		txtField.setText("");
		btn.setEnabled(true);
	}
	
	public void setEnable(Boolean enabled) {
		btn.setEnabled(enabled);
	}
	
	public Boolean showExecutedRules() {
		return showExecutedRules.isSelected();
	}

}
