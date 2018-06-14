package ru.ulpfr.pension_brms.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.ulpfr.pension_brms.managers.InputDataManager;
import ru.ulpfr.pension_brms.model.FactsStore;

@SuppressWarnings("unchecked")
public class FactsBuilder {

	private List<Object> facts;
	
	public FactsBuilder() {
		facts = new ArrayList<>();
		createFacts();
	}
	
	private void createFacts() {
		FactsStore store = InputDataManager.getInstance().getXmlData();
		if(store == null)
			return;
		Field[] fields = store.getClass().getDeclaredFields();
		for ( Field field : fields  ) {
		   try {
		     field.setAccessible(true);
		     Object obj = field.get(store);
		     if(obj instanceof Collection) {
		    	 facts.addAll((Collection<Object>)obj);
		     } else {
		    	 facts.add(obj); 
		     }
		   } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		   } catch (IllegalArgumentException e) {
			   e.printStackTrace();
		   }
		 }
		 for(Object obj: facts) {
			 System.out.println(obj.getClass().toString()); 
		 }
	}
	
	
	public List<Object> build() {
	        return facts;
	}

}
