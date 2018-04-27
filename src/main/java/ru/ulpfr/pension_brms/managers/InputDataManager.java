package ru.ulpfr.pension_brms.managers;

import java.io.File;
import java.util.List;

import ru.ulpfr.pension_brms.utils.XmlFileReader;

public class InputDataManager {
	/**
	 * Класс для манипуляций всеми вxодными данными
	 */
	
	private String var_settings;
	private List<Object> input_xml;
	private XmlFileReader xml_fr;
	
	private static InputDataManager instance;
	
	public InputDataManager() {
		xml_fr = new XmlFileReader();
	}
	
	public static synchronized InputDataManager getInstance() {
		if (instance == null) {
			instance = new InputDataManager();
		}
		return instance;
	}
	
	public Boolean parseXmlFile(File fl , Boolean validate) {
		try {
			input_xml = xml_fr.readFromFile(fl);
			if (input_xml != null) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}	
	}

}
