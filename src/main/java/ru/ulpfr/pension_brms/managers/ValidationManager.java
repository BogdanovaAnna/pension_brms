package ru.ulpfr.pension_brms.managers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.ulpfr.pension_brms.model.InputVariable;
import ru.ulpfr.pension_brms.model.ValidationErrors;
import ru.ulpfr.pension_brms.model.XmlBlock;

public class ValidationManager {
	
	private static ValidationManager instance;
	private List<String> errors;
	

	public ValidationManager() {
	}
	
	public static synchronized ValidationManager getInstance() {
		if (instance == null) {
			instance = new ValidationManager();
		}
		return instance;
	}
	
	public List<String> validateXML( List<XmlBlock> data, List<InputVariable> config) {
		errors = new ArrayList<String>();
		for (XmlBlock obj : data) {
			Map<String, String> tags = obj.getTags();
			for(Map.Entry<String, String> entry : tags.entrySet()) {
				List<InputVariable> result =config.stream().filter(item -> item.getName().equals(entry.getKey())).collect(Collectors.toList());
				if(result.size() < 1) {
					errors.add(ValidationErrors.PARAM_NOT_FOUNDED + " "+entry.getKey());
				} else {
					InputVariable last = result.get(result.size() - 1);
					checkType(last, entry.getValue(), last.getName());
					checkAllowedValues(last.getValues(), entry.getValue(), last.getName());
				}
					
			}
			
			for (InputVariable param : config) {
				String tag_name = param.getName();
				if(obj.checkTag(tag_name)) {
					
				}
			}
				
		}
		return errors;	
	}
	
	private void checkType(InputVariable param, String value, String tag) {
		String allowed_type = param.getType();
		switch (allowed_type.trim()) {
		case "int": 
		case "integer":
			try {
				Integer.valueOf(value);
			} catch (NumberFormatException e){
				errors.add(ValidationErrors.INCORRECT_TYPE + " : "+tag+" = "+value+" типа "+value.getClass().getSimpleName()+" вместо "+allowed_type);
			}
			
			break;
			
		case "float": 
			try {
				Float.valueOf(value);
			} catch (NumberFormatException e){
				errors.add(ValidationErrors.INCORRECT_TYPE + " : "+tag+" = "+value+" типа "+value.getClass().getSimpleName()+" вместо "+allowed_type);
			}
			break;
		case "date": 
			try {
			    SimpleDateFormat sdf = new SimpleDateFormat(param.getFormat());
			    Date date = sdf.parse(value);
			    if (!value.equals(sdf.format(date))) {
			    	errors.add(ValidationErrors.INCORRECT_TYPE + " : "+tag+" = "+value+" не является "+allowed_type);
			    }
			} catch (ParseException ex) {
				errors.add(ValidationErrors.INCORRECT_DATE_FORMAT + " : "+tag+" = "+value+" не по шаблону "+param.getFormat());
			}
			break;
		}
		
	}
	
	private void checkAllowedValues(List<String> valArr, String value, String tag) {
		if(valArr ==null)
			return;
		Boolean matched = false;
		for (String str : valArr) {
			if(str.equals(value))
				matched = true;
		}
		
		if(!matched)
			errors.add(ValidationErrors.IN_ALLOWED_VALUES + " : "+tag+" = "+value+" вместо "+valArr.toString());
	}

}
