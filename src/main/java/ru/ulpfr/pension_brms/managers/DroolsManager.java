package ru.ulpfr.pension_brms.managers;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.io.impl.ClassPathResource;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.MainWindow.TABS;
import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;
import ru.ulpfr.pension_brms.listeners.TrackingAgendaEventListener;
import ru.ulpfr.pension_brms.utils.ClientBuilder;
import ru.ulpfr.pension_brms.utils.FactsBuilder;

public class DroolsManager {
	
	private KieSession kSession;
	private KnowledgeBuilder kbuilder;
	private KieSessionConfiguration config;
	private TrackingAgendaEventListener agendaEventListener;
	private List<Object> facts;
	private static DroolsManager instance;
	
	//Типы файловыx ресурсов с правилами для обработки в Rules Engine
	private enum RULES_TYPES {
		DRL,
		DSL,
		DTABLE
	}
	
	public DroolsManager () {
		initEngine(RULES_TYPES.DSL);
	}
	
	public static synchronized DroolsManager getInstance() {
		if (instance == null) {
			instance = new DroolsManager();
		}
		return instance;
	}
	
		
	//Инициализация Rules Engine и проверка текущей базы знаний
	private void initEngine(RULES_TYPES rulesType) {
		try {
			MainWindow.output("Инициализация Rules Engine", MESSAGE_TYPE.SYSTEM);
			kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			config = KieServices.Factory.get().newKieSessionConfiguration();
			
			switch (rulesType) {
			case DSL:
				kbuilder.add(new ClassPathResource("dsl/pens_rules.dsl"), ResourceType.DSL);
				kbuilder.add(new ClassPathResource("dsl/pens_rules.dslr"), ResourceType.DSLR);
				break;
				
			case DRL:
			default:
				kbuilder.add(new ClassPathResource("rules/pens_rules.drl"), ResourceType.DRL);
				break;
			}
			
			if ( kbuilder.hasErrors() ) {
				System.out.println(kbuilder.getErrors().toString());
				MainWindow.output("Не удалось скомпилировать правила", MESSAGE_TYPE.ERROR);
				MainWindow.output(kbuilder.getErrors().toString(), MESSAGE_TYPE.ERROR);
			} else {
				MainWindow.output("Правила успешно скомпилированы", MESSAGE_TYPE.SYSTEM);
				}	
		} catch (Throwable t) {
			t.printStackTrace();
			MainWindow.output(t.getClass().toString() +" :: "+t.getMessage(), MESSAGE_TYPE.ERROR);
		}
			
	}
		
	//создание новой сессии для каждой загруженной XML
	private void initSession() {
		if(kbuilder.hasErrors()) {
			return;
		}	
		KieBase kBase = kbuilder.newKieBase();
		kSession = kBase.newKieSession(config, null);
	}
	
	private void setListeners() {
		if(kSession != null) {
			//Устанавливаем слушатель отработанныx правил
			agendaEventListener = new TrackingAgendaEventListener();
			kSession.addEventListener(agendaEventListener);
		}
	}
	
	//Создаем факты на основе XML-данныx
	private void initFactsFromXML()  {	
		if(kSession != null) {	
			FactsBuilder fbuilder = new FactsBuilder();
			facts = fbuilder.build();
		} else 
			MainWindow.output("Сессия для обработки правил не была создана. Процесс создания фактов не запущен", MESSAGE_TYPE.ERROR);
	}
	
	//Добавляем факты в RE и запускаем отработку правил
	private void fireRules() {
		if(facts != null && facts.size() > 0) {
			for (Object fact : facts) {
				kSession.insert(fact);
			}
			MainWindow.output("<<----Процесс обработки фактов запущен---->>", MESSAGE_TYPE.INFO);
			kSession.fireAllRules();			
		} else 
			MainWindow.output("Нет фактов для добавления в Rules Engine. Процесс обработки остановлен.", MESSAGE_TYPE.ERROR);
	}

	//запуск процесса обработки правил
	public void execute( TABS mode) {
		try {
			initSession();
			setListeners();
			switch (mode) {
			case XML:
				initFactsFromXML();
				break;

			case RUNTIME:
				facts = new ArrayList<Object>();
				facts.add(new ClientBuilder().build());
				break;
			}
			
			fireRules();
			//MainWindow.output(agendaEventListener.matchsToString(), MESSAGE_TYPE.INFO); //вывод отработанныx правил	
		} catch (Throwable t) {
			t.printStackTrace();
			MainWindow.output(t.getClass().toString() +" :: "+t.getMessage(), MESSAGE_TYPE.ERROR);
		} 
	}
	
	
}
