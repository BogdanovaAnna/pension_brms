package ru.ulpfr.pension_brms.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;
import ru.ulpfr.pension_brms.model.Pension;
import ru.ulpfr.pension_brms.model.XmlBlock;
import ru.ulpfr.pension_brms.utils.ClientBuilder;

public class DroolsManager {
	
	private KieSession kSession;
	private List<Object> facts;
	private static DroolsManager instance;
	
	public static synchronized DroolsManager getInstance() {
		if (instance == null) {
			instance = new DroolsManager();
		}
		return instance;
	}
	
	private void initSession() {
		try {
			//загрузка базы знаний и создание проверочной сессии
	        KieServices ks = KieServices.Factory.get();
		    KieContainer kContainer = ks.getKieClasspathContainer();
	    	kSession = kContainer.newKieSession("pens_session");
		}  catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	private void initFacts()  {
		
		facts = new ArrayList<>();
		List<XmlBlock> clients = InputDataManager.getInstance().getXmlClients();
		for (Iterator<XmlBlock> iterator = clients.iterator(); iterator.hasNext();) {
			XmlBlock xmlBlock = (XmlBlock) iterator.next();
			Map<String, String> xmlProps = xmlBlock.getTags();
			ClientBuilder client = new ClientBuilder();
			for (Map.Entry<String, String> entry : xmlProps.entrySet())
			{
			    System.out.println(entry.getKey() + "/" + entry.getValue());
			    try {
			    	switch (entry.getKey()) {
					case "Vid_Pens":
						facts.add(new Pension(client.build().getId(), Integer.valueOf(entry.getValue())));
						break;
					case "GragdRF":
						client.withNationality(Integer.valueOf(entry.getValue()));
						break;
					case "Gender":
						client.withGender(entry.getValue());
						break;
					case "D_Rogd":
						client.withDateBirth(entry.getValue());
						System.out.println("Age = "+client.build().getAge());
						break;
					case "Stag_ZL":
						client.withWorkExperience(entry.getValue());
						break;
					case "IPK_ZL":
						client.withIPK(Float.valueOf(entry.getValue()));
						break;
					case "St_PK":
						client.withIPKCost(Float.valueOf(entry.getValue()));
						break;
					default:
						client.withProperty(entry.getKey(), entry.getValue());
						break;
					}
				} catch (Exception e) {
					MainWindow.getInstance().output(e.getClass().getName() + " : "+ e.getMessage(), MESSAGE_TYPE.ERROR);
					facts = null;
					return;
				}
				
			}
			
			facts.add(client.build());
			
		}
	}
	
	private void fireRules() {
		if(kSession !=null && facts != null && facts.size() > 0) {
			for (Object fact : facts) {
				kSession.insert(fact);
			}
			kSession.fireAllRules();
		}
	}
	
	public void execute() {
		try {
			initSession();
			initFacts();
			fireRules();
			
	    } catch (Throwable t) {
	        t.printStackTrace();
	    } 
	}
}
