package ru.ulpfr.pension_brms.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import ru.ulpfr.pension_brms.model.InputVariable;
import ru.ulpfr.pension_brms.utils.XmlFileReader;

public class InputDataManager {
	/**
	 * Класс для манипуляций всеми вxодными данными
	 */
	
	private String var_settings;
	private List<Object> input_xml;
	private XmlFileReader xmlFR;
	
	private static InputDataManager instance;
	
	public InputDataManager() {
		xmlFR = new XmlFileReader();
	}
	
	public static synchronized InputDataManager getInstance() {
		if (instance == null) {
			instance = new InputDataManager();
		}
		return instance;
	}
	
	public Boolean parseXmlFile(File fl , Boolean validate) {
		try {
			input_xml = xmlFR.readFromFile(fl);
			if (input_xml != null) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}	
	}
	
	public void parseInputVarsJSON() {
		
		String path = this.getClass().getClassLoader().getResource("configs/input_vars.json").getFile();
		Gson gson = new Gson();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			List<InputVariable> variables = gson.fromJson(br, new TypeToken<List<InputVariable>>(){}.getType());
			for (InputVariable var : variables) {
				System.out.print(var.getName()+'\n');
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
	        e.printStackTrace();
	    } catch (JsonSyntaxException e) {
	        e.printStackTrace();
	    }
	}

}
