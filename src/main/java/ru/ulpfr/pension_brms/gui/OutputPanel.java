package ru.ulpfr.pension_brms.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class OutputPanel extends JPanel {

	/**
	 * Панель для вывода информации из Rules Engine
	 */
	private static final long serialVersionUID = 1750392838690660219L;
	private JTextPane textArea;	
	private JButton clearBtn;
	public static enum MESSAGE_TYPE { INFO, ERROR, SYSTEM, RULES}

	public OutputPanel() {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setPreferredSize(new Dimension(460, 500));
		setupTextPane();
		setupClearBtn();
	}
	
	private void setupTextPane() {
		textArea = new JTextPane();
		textArea.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		textArea.setPreferredSize(new Dimension(445, 445));
		textArea.addFocusListener(new FocusListener() {		
			@Override
			public void focusLost(FocusEvent e) {
				textArea.setEditable(true);
			}
			@Override
			public void focusGained(FocusEvent e) {
				textArea.setEditable(false);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setColumnHeaderView(new JLabel("Окно вывода результатов обработки данныx"));
		add(scrollPane);
	}
	
	private void setupClearBtn() {
		clearBtn = new JButton("Очистить");
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearTextPane();
			}
		});
		add(clearBtn);
	}
	
	public void clearTextPane() {
		if(textArea != null)
			textArea.setText("");
	}
	
	public void appendMsg(String msg) {
		appendMsg(msg, MESSAGE_TYPE.INFO);
	}
	
	public void appendMsg(String msg, MESSAGE_TYPE type) {
		Color _color;
		switch (type) {
		case RULES:
			_color = new Color(0,155,0);
			break;
		case SYSTEM:
			_color = Color.BLUE;
			break;
		case ERROR:
			_color = Color.RED;
			break;
		default:
			_color = Color.BLACK;
			break;
		}
		appendToPane("["+ type+"] :: " + msg, _color);
		textArea.repaint();
	}
	
	
	private void appendToPane(String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
	    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
	    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
	    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
	    int len = textArea.getDocument().getLength();
	    textArea.setCaretPosition(len);
	    textArea.setCharacterAttributes(aset, false);
	    textArea.replaceSelection(msg+'\n');
	}

}
