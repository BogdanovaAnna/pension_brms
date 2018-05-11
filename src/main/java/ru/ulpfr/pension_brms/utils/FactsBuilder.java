package ru.ulpfr.pension_brms.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.OutputPanel.MESSAGE_TYPE;
import ru.ulpfr.pension_brms.managers.InputDataManager;
import ru.ulpfr.pension_brms.model.Pension;
import ru.ulpfr.pension_brms.model.XmlBlock;

public class FactsBuilder {

	private List<Object> facts;
	
	public FactsBuilder() {
		facts = new ArrayList<>();
		createFacts();
	}
	
	private void createFacts() {
		List<XmlBlock> clients = InputDataManager.getInstance().getXmlClients();
		
		for (Iterator<XmlBlock> iterator = clients.iterator(); iterator.hasNext();) {
			XmlBlock xmlBlock = (XmlBlock) iterator.next();
			Map<String, String> xmlProps = xmlBlock.getTags();
			
			/*Парсинг xml-данныx в факты*/
			ClientBuilder client = new ClientBuilder();
			facts.add(client.build());
			
			for (Map.Entry<String, String> entry : xmlProps.entrySet())
			{
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
					MainWindow.output(e.getClass().getName() + " : "+ e.getMessage() + " in variable "+entry.getKey(), MESSAGE_TYPE.ERROR);
					facts = null;
				}
				
			}
		}
		
	}
	
	
	public List<Object> build() {
	        return facts;
	}

}
