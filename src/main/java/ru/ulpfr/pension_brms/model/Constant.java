package ru.ulpfr.pension_brms.model;

public class Constant {
	
	private String name;
	private Object value;
	private String desc;

	public Constant() {
	}
	
	public Constant(String _name, Object _value, String _desc) {
		this.name = _name;
		this.value = _value;
		this.desc = _desc;
	}
	
	public void setName(String _name) {
		this.name = _name;
	}
	public void setValue(Object _value) {
		this.value = _value;	
	}
	public void setDesc(String _desc) {
		this.desc = _desc;
	}
	
	public String getName() {
		return this.name;
	}
	public String getDesc() {
		return this.desc;
	}
	
	public Object getValue() {
		return this.value;
	}

}
