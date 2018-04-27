package ru.ulpfr.pension_brms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class InputVariable implements Serializable {
	/**
	 * Объект вxодного параметра из конфигурационного файла input_vars.json
	 */
	private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String desc;
    private String format;
    private ArrayList<Object> values;
    private ArrayList<String> values_desc;
    private Object default_value;
    
	public InputVariable() {
	}
	
	public InputVariable(String name, String type, String desc) {
		this.name = name;
		this.type = type;
		this.desc = desc;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return type;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Object getDefault() {
		return default_value;
	}

	public void setDefault(Object value) {
		this.default_value = value;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String value) {
		this.format = value;
	}
	
	public ArrayList<Object> getValues() {
		return values;
	}

	public void setValues(ArrayList<Object> arr) {
		this.values = arr;
	}
	
	public ArrayList<String> getValuesDesc() {
		return values_desc;
	}

	public void setValuesDesc(ArrayList<String> arr) {
		this.values_desc = arr;
	}
	
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InputVariable other = (InputVariable) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InputVariable{" + "name=" + name + ", type=" + type
                + ", desc=" + desc + '}';
    }

}
