package ru.ulpfr.pension_brms.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;
import ru.ulpfr.pension_brms.model.Constant;
import ru.ulpfr.pension_brms.model.Constants;
import ru.ulpfr.pension_brms.model.InputVariable;
import ru.ulpfr.pension_brms.model.XmlBlock;
import ru.ulpfr.pension_brms.utils.CSVReader;
import ru.ulpfr.pension_brms.utils.XmlFileReader;

public class InputDataManager {
	/**
	 * Класс для манипуляций всеми вxодными данными
	 */
	private XmlFileReader xmlFR;
	private static InputDataManager instance;
	
	private List<XmlBlock> xml_vars; //параметры из загруженной XML
	private List<InputVariable> json_vars; //параметры из input_vars.json
	
	public enum READER_STATUS { SUCCESS, ERROR_SINTAX, INVALID_DATA, INVALID_TAG_STRUCTURE}
	
	public InputDataManager() {
		xmlFR = new XmlFileReader();
	}
	
	public static synchronized InputDataManager getInstance() {
		if (instance == null) {
			instance = new InputDataManager();
		}
		return instance;
	}
	
	//Доработать!!!!!!!
	public READER_STATUS parseXmlFile(File fl , Boolean validate_xml) {
		try {
			xml_vars = xmlFR.readFromFile(fl);
			if (xml_vars != null) {
					if(validate_xml) {
						List<String> errors = ValidationManager.getInstance().validateXML(xml_vars, json_vars);
						if(errors.size() == 0)
							return READER_STATUS.SUCCESS;
						MainWindow.getInstance().output(errors, MESSAGE_TYPE.ERROR);
						return READER_STATUS.INVALID_DATA;
					} else
					return READER_STATUS.SUCCESS;
			} else
				return READER_STATUS.INVALID_TAG_STRUCTURE;
		} catch (Exception e) {
			e.printStackTrace();
			return READER_STATUS.ERROR_SINTAX;
		}	
	}
	
	public void parseInputVarsJSON() {
		String path = this.getClass().getClassLoader().getResource("configs/input_vars.json").getFile();
		Gson gson = new Gson();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			json_vars = gson.fromJson(br, new TypeToken<List<InputVariable>>(){}.getType());
			MainWindow.getInstance().output("Файл input_vars.json успешно обработан", MESSAGE_TYPE.SYSTEM);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
	        e.printStackTrace();
	    } catch (JsonSyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	public void parseConstantsCSV () {
		try {
			CSVReader reader = new CSVReader("configs/constants.csv");
			ArrayList<List<String>> dataTable = reader.getLoadedData();
			for (List<String> line : dataTable) {
				String desc = (line.size() < 3) ? "": line.get(2);		
				Constants.addConstant(new Constant(line.get(0), line.get(1), desc));
			}
			MainWindow.getInstance().output("Файл constants.csv успешно обработан", MESSAGE_TYPE.SYSTEM);
		} catch (Exception e) {
			MainWindow.getInstance().output("Файл constants.csv не загружен. "+e.getMessage(), MESSAGE_TYPE.ERROR);
		}
		
	}
	
	public InputVariable getInputVariableByName (String name) {
		if(json_vars != null && json_vars.size() > 0) {
			for (InputVariable variable : json_vars) {
				if(variable.getName().equals(name))
					return variable;
			}
		}
		return null;
	}
	
	public List<XmlBlock> getXmlClients() {
		return xml_vars;
	}

}
