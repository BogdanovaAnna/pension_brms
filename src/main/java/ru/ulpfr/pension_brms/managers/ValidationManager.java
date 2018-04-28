package ru.ulpfr.pension_brms.managers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ulpfr.pension_brms.model.InputVariable;
import ru.ulpfr.pension_brms.model.ValidationErrors;
import ru.ulpfr.pension_brms.model.XmlBlock;

public class ValidationManager {
	
	private static ValidationManager instance;
	private List<String> errors;
	

	public ValidationManager() {
		errors = new ArrayList<String>();
	}
	
	public static synchronized ValidationManager getInstance() {
		if (instance == null) {
			instance = new ValidationManager();
		}
		return instance;
	}
	
	public List<String> validateXML( List<XmlBlock> data, List<InputVariable> config) {
		for (InputVariable param : config) {
			for (XmlBlock obj : data) {
				String tag_name = param.getName();
				if(obj.checkTag(tag_name)) {
					checkType(param, obj.getValue(tag_name));
					checkAllowedValues(param.getValues(), obj.getValue(tag_name));
				}
			}
		}
		return errors;
		
	}
	
	private void checkType(InputVariable param, String value) {
		String allowed_type = param.getType();
		switch (allowed_type.trim()) {
		case "int": 
		case "integer":
			try {
				Integer.valueOf(value);
			} catch (NumberFormatException e){
				errors.add(ValidationErrors.INCORRECT_TYPE + " : ["+value+"] as "+allowed_type);
			}
			
			break;
			
		case "float": 
			try {
				Float.valueOf(value);
			} catch (NumberFormatException e){
				errors.add(ValidationErrors.INCORRECT_TYPE + " : ["+value+"] as "+allowed_type);
			}
			break;
		case "date": 
			try {
			    SimpleDateFormat sdf = new SimpleDateFormat(param.getFormat());
			    Date date = sdf.parse(value);
			    if (!value.equals(sdf.format(date))) {
			    	errors.add(ValidationErrors.INCORRECT_TYPE + " : ["+value+"] as "+allowed_type);
			    }
			} catch (ParseException ex) {
				errors.add(ValidationErrors.INCORRECT_DATE_FORMAT + " : ["+value+"] as "+param.getFormat());
			}
			break;
		}
		
	}
	
	private void checkAllowedValues(List<String> valArr, String value) {
		if(valArr ==null)
			return;
		Boolean matched = false;
		for (String str : valArr) {
			if(str.equals(value))
				matched = true;
		}
		
		if(!matched)
			errors.add(ValidationErrors.IN_ALLOWED_VALUES + " : ["+value+"] in "+valArr.toString());
	}

}
