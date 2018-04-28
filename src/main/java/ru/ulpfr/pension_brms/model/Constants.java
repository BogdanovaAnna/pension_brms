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
	
	public static List<Constant>  getConstants() {
			return constants;
	}
	

}
