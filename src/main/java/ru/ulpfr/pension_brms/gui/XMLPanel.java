package ru.ulpfr.pension_brms.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import ru.ulpfr.pension_brms.managers.InputDataManager;

public class XMLPanel extends JPanel {
	
	/**
	 * Панель для загрузки XML-файлов
	 */
	private static final long serialVersionUID = -2482425422212218523L;
	private JFileChooser fch;
	private JButton btn;
	private JTextField txtField, statusLabel;
	private JCheckBox validateFile;
	private Box topBox, middleBox, bottomBox;

	public XMLPanel() {
		setBorder(BorderFactory.createTitledBorder("Выберете XML-файл для обработки"));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(460, 120));
		initFileChooser();
		initPathText();
		initCheckBoxOption();
		initStatusText();
		initFileButton();
		initBoxes();
		addComponents();	
	}
	
	private void initFileChooser() {
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
				int returnValue = fch.showDialog(null, "Выбрать");
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fch.getSelectedFile();
					txtField.setText(selectedFile.getAbsolutePath());
					//загоняем выбранный файл в менеджер обработки вxодныx данныx
					if(InputDataManager.getInstance().parseXmlFile(selectedFile, validateFile.isSelected())) {
						updateStatus("файл успешно обработан");
					} else
						updateStatus("не удалось обработать файл");
					
				}
				btn.setEnabled(true);
				
			}
		});	
	}
	
	private void initCheckBoxOption() {
		validateFile = new JCheckBox("Валидация xml");
		validateFile.setSelected(true);
	}
	
	private void initPathText() {
		txtField = new JTextField("Путь к xml-файлу отсутствует");
		txtField.setColumns(26);
		txtField.setEditable(false);
	}
	
	private void initStatusText() {
		statusLabel = new JTextField();
		statusLabel.setPreferredSize(new Dimension(420, 22));
		Font font = new Font("Courier", Font.BOLD,13);
		statusLabel.setFont(font);
		statusLabel.setBackground(new Color(229, 236, 255));
		statusLabel.setEditable(false);
		updateStatus("файл не выбран");
	}
	
	private void initBoxes() {
		topBox = Box.createHorizontalBox();
		middleBox = Box.createHorizontalBox();
		bottomBox = Box.createVerticalBox();
		topBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		middleBox.setBorder(BorderFactory.createEmptyBorder(0, 315, 0, 0));
		bottomBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		topBox.add(btn);
		topBox.add(txtField);
		middleBox.add(validateFile);
		bottomBox.add(statusLabel);
		
	}
	
	public void addComponents() {
		this.add(topBox);
		this.add(middleBox);
		this.add(bottomBox);
	}
	
	public void updateStatus(String str) {
		if( statusLabel != null ) {
			statusLabel.setText("Статус: "+ str);
		}
	}
	
	public void reset() {
		updateStatus("файл не выбран");
		txtField.setText("");
		btn.setEnabled(true);
	}

}
