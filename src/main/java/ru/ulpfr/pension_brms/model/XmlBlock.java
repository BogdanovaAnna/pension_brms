package ru.ulpfr.pension_brms.model;

import java.util.HashMap;
import java.util.Map;

public class XmlBlock {
	
	private Map<String, String> tags;
	
	public XmlBlock() {
		tags = new HashMap<String, String>();
	}
	
	public Map<String, String> getTags() {
		return this.tags;
	}

	public void setProps(Map<String, String> list) {
		this.tags = list;
	}
	    
	public String setValue(String tag_name, String value) {
		return this.tags.put(tag_name, value);
	}

	public String getValue(String tag_name) {
		return this.tags.getOrDefault(tag_name, null);
	}
	
	public Boolean checkTag(String tag_name) {
		if(this.tags.getOrDefault(tag_name, null) != null)
			return true;
		return false;
	}
	
	

}
