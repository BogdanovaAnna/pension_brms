package ru.ulpfr.pension_brms.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;
import ru.ulpfr.pension_brms.model.Constant;
import ru.ulpfr.pension_brms.model.Constants;
import ru.ulpfr.pension_brms.model.FactsStore;
import ru.ulpfr.pension_brms.model.InputVariable;
import ru.ulpfr.pension_brms.utils.CSVReader;
import ru.ulpfr.pension_brms.utils.JAXBConverter;

public class InputDataManager {
	/**
	 * Класс для манипуляций всеми вxодными данными
	 */
	private static InputDataManager instance;
	
	private List<InputVariable> json_vars; //параметры из input_vars.json
	private FactsStore fstore; // корневой класс объектов из XML
	
	public enum READER_STATUS { SUCCESS, INIT, ERROR_SINTAX, INVALID_DATA, INVALID_TAG_STRUCTURE}
	private Boolean allReady = true;
	
	public static synchronized InputDataManager getInstance() {
		if (instance == null) {
			instance = new InputDataManager();
		}
		return instance;
	}
	
	public READER_STATUS parseXmlFile(File fl) {
		try {
			fstore = new JAXBConverter().unmarshallFacts(fl);
			if (fstore != null) {
					return READER_STATUS.SUCCESS;
			} else
				return READER_STATUS.INVALID_TAG_STRUCTURE;
		} catch (Exception e) {
			e.printStackTrace();
			return READER_STATUS.ERROR_SINTAX;
		}	
	}
	
	public void parseInputVarsJSON() {
		try {
			InputStream is = InputDataManager.class.getResourceAsStream("/configs/input_vars.json");
			if(is == null)
				return;
			Gson gson = new Gson();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(isr);
			json_vars = gson.fromJson(br, new TypeToken<List<InputVariable>>(){}.getType());
			MainWindow.output("Файл input_vars.json успешно обработан", MESSAGE_TYPE.SYSTEM);
			setReady(true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setReady(false);
			MainWindow.output("Файл input_vars.json не загружен. "+e.getClass().getSimpleName().toString() + " : "+e.getMessage(), MESSAGE_TYPE.ERROR);
		} 
	}
	
	public void parseConstantsCSV () {
		try {
			CSVReader reader = new CSVReader("/configs/constants.csv");
			ArrayList<List<String>> dataTable = reader.getLoadedData();
			for (List<String> line : dataTable) {
				if(line.size() < 2)
					throw new Exception("Неверный формат содержимого - нет значений, разделенныx через ; в строке: "+line);
				String desc = (line.size() < 3) ? "": line.get(2);		
				Constants.addConstant(new Constant(line.get(0), line.get(1), desc));
			}
			MainWindow.output("Файл constants.csv успешно обработан", MESSAGE_TYPE.SYSTEM);
		} catch (Exception e) {
			e.printStackTrace();
			this.setReady(false);
			MainWindow.output("Файл constants.csv не загружен. "+e.getClass().getSimpleName().toString() + " : "+e.getMessage(), MESSAGE_TYPE.ERROR);
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
	
	public FactsStore getXmlData() {
		return fstore;
	}
	
	public void setReady(Boolean val) {
		allReady = val;
		System.out.println("setReady = "+ val);
		if(getReady()) { //все конфигурационные файлы скачались - сообщаем, что всё готово
			ApplicationManager.getInstance().mainWindowReady();
		} else {
			ApplicationManager.getInstance().mainWindowNotReady();
		}
	}
	
	public Boolean getReady() {
		return allReady;
	}

}
