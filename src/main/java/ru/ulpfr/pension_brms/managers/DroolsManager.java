package ru.ulpfr.pension_brms.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;
import ru.ulpfr.pension_brms.model.Pension;
import ru.ulpfr.pension_brms.model.XmlBlock;
import ru.ulpfr.pension_brms.utils.ClientBuilder;

public class DroolsManager {
	
	private KieSession kSession;
	private KnowledgeBuilder kbuilder;
	private KieSessionConfiguration config;
	private List<Object> facts;
	private static DroolsManager instance;
	
	public DroolsManager () {
		initEngine();
	}
	
	public static synchronized DroolsManager getInstance() {
		if (instance == null) {
			instance = new DroolsManager();
		}
		return instance;
	}
	
	//Создаем факты
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
					MainWindow.getInstance().output(e.getClass().getName() + " : "+ e.getMessage() + " in variable "+entry.getKey(), MESSAGE_TYPE.ERROR);
					facts = null;
					return;
				}
				
			}
			
			facts.add(client.build());
			
		}
	}
	
	//Добавляем факты в RE и запускаем отработку правил
	private void fireRules() {
		if(kSession !=null && facts != null && facts.size() > 0) {
			for (Object fact : facts) {
				kSession.insert(fact);
			}
			MainWindow.getInstance().output("<<----Процесс обработки фактов запущен---->>", MESSAGE_TYPE.INFO);
			kSession.fireAllRules();
		}
	}
	
	//Инициализация Rules Engine и проверка текущей базы знаний
	private void initEngine() {
		try {
			MainWindow.getInstance().output("Инициализация Rules Engine", MESSAGE_TYPE.SYSTEM);
			kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			config = KieServices.Factory.get().newKieSessionConfiguration();
			
			//kbuilder.add( ResourceFactory.newClassPathResource( "rules/pens_rules.drl", getClass() ), ResourceType.DRL );
			kbuilder.add( ResourceFactory.newClassPathResource( "dsl/pens_rules.dsl", getClass()), ResourceType.DSL );
			kbuilder.add(ResourceFactory.newClassPathResource( "dsl/pens_rules.dslr", getClass()), ResourceType.DSLR );
			if ( kbuilder.hasErrors() ) {
				System.out.println(kbuilder.getErrors().toString());
				MainWindow.getInstance().output("Не удалось скомпилировать правила", MESSAGE_TYPE.ERROR);
				MainWindow.getInstance().output(kbuilder.getErrors().toString(), MESSAGE_TYPE.ERROR);
			} else {
				MainWindow.getInstance().output("Правила успешно скомпилированы", MESSAGE_TYPE.SYSTEM);
			}	
		} catch (Throwable t) {
            t.printStackTrace();
            MainWindow.getInstance().output(t.getClass().toString() +" :: "+t.getMessage(), MESSAGE_TYPE.ERROR);
        }
		
	}
	
	//создание новой сессии для каждой загруженной XML
	private void initSession() {
		if(kbuilder.hasErrors())
			return;
		KieBase kBase = kbuilder.newKieBase();
	    kSession = kBase.newKieSession(config, null);
	}

	public void execute() {
		try {
			initSession();
			initFacts();
			fireRules();
			
	    } catch (Throwable t) {
	        t.printStackTrace();
	        MainWindow.getInstance().output(t.getClass().toString() +" :: "+t.getMessage(), MESSAGE_TYPE.ERROR);
	    } 
	}
}
