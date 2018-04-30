package ru.ulpfr.pension_brms.model;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	
	private static List<Constant> constants = new ArrayList<Constant>();
	
	public static void addConstant(Constant constant) {
		constants.add(constant);
	}
	
	public static Constant getConstant(String name) {
		if(constants != null) {
			for (Constant cnst : constants) {
				if(cnst.getName().equals(name))
					return cnst;
			}
		}
		return null;

	}
	
	public static String getConstantValue(String name) {
		if(constants != null) {
			for (Constant cnst : constants) {
				if(cnst.getName().equals(name))
					return cnst.getValue().toString();
			}
		}
		return null;
	}
	
	public static Long getConstantNumValue(String name) {
		String value = getConstantValue(name);
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			return 0L;
		}
	}
	
	public static List<Constant>  getConstants() {
			return constants;
	}
	

}
