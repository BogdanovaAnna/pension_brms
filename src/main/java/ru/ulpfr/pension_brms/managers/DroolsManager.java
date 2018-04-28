package ru.ulpfr.pension_brms.managers;

import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsManager {
	
	private KieSession kSession;
	private List<Object> facts;
	
	private void initSession() {
		try {
			//загрузка базы знаний и создание проверочной сессии
	        KieServices ks = KieServices.Factory.get();
		    KieContainer kContainer = ks.getKieClasspathContainer();
	    	kSession = kContainer.newKieSession("base-rules");
		}  catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	private void initFacts() {
		
	}
	
	private void addFacts() {
		if(kSession !=null && facts != null && facts.size() > 0) {
			for (Object fact : facts) {
				kSession.insert(fact);
			}
		}
	}
	
	public void execute() {
		try {
			initSession();
			initFacts();
			addFacts();
			kSession.fireAllRules();
	    } catch (Throwable t) {
	        t.printStackTrace();
	    }
	}
}
