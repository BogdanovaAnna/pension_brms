package ru.ulpfr.pension_brms.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.ulpfr.pension_brms.listeners.GlobalListener;
import ru.ulpfr.pension_brms.managers.DroolsManager.RULES_TYPES;

public class ApplicationManager {
	private List<GlobalListener> listeners = new ArrayList<GlobalListener>();
	private static ApplicationManager instance;
	
	public static synchronized ApplicationManager getInstance() {
		if (instance == null) {
			instance = new ApplicationManager();
		}
		return instance;
	}
	
	 public void addListener(GlobalListener toAdd) {
		listeners.add(toAdd);
		System.out.println("add listener: "+toAdd);
	 }
	 public void removeListener(GlobalListener toRemove) {
	    for (Iterator<GlobalListener> iterator = listeners.listIterator(); iterator.hasNext(); ) {
	       GlobalListener listener = iterator.next();
	       if (listener.equals(toRemove)) {
	    	   iterator.remove();
	    	   System.out.println("remove listener: "+toRemove);
	       }
	    }
	  }

	 public void mainWindowReady() {
	        System.out.println("Main window is ready");
	        if(!listeners.isEmpty()) {
	        	for (GlobalListener rl : listeners)
	                rl.windowReady();
	        }
	 }
	    
	 public void mainWindowNotReady() {
	        System.out.println("Main window is not ready");
	        if(!listeners.isEmpty()) {
		        for (GlobalListener rl : listeners)
		            rl.windowNotReady();
	        }
	 }
	    
	    public void startDrools(RULES_TYPES mode) {
	    	System.out.println("Starting Drools on mode "+mode);
	        if(!listeners.isEmpty()) {
	        	System.out.println("!listeners.isEmpty() ");
		        for (GlobalListener rl : listeners)
		            rl.allowDroolsInit(mode);
	        }
	    }
	
}
