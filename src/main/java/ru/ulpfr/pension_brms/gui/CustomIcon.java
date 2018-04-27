package ru.ulpfr.pension_brms.gui;

import javax.swing.ImageIcon;

public class CustomIcon extends ImageIcon {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1642013649605586559L;
	public static final String ERROR = "error.png";
	public static final String SUCCESS = "ok.png";

	public CustomIcon(String arg0) {
		super(CustomIcon.class.getClassLoader().getResource("/images/"+arg0).getPath());
	}
	


}
