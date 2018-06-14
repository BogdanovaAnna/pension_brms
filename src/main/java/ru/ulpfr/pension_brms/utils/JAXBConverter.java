package ru.ulpfr.pension_brms.utils;

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ru.ulpfr.pension_brms.model.FactsStore;
import ru.ulpfr.pension_brms.model.rules.Client;

public class JAXBConverter {
	
	public void marshallFacts(FactsStore facts) {
		  try {
		   JAXBContext context = JAXBContext.newInstance(FactsStore.class);
		   Marshaller marshaller = context.createMarshaller();
		   marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		   marshaller.marshal(facts, System.out);
		  } catch (JAXBException exception) {
		   Logger.getLogger(JAXBConverter.class.getName()).
		      log(Level.SEVERE, "marshallExample threw JAXBException", exception);
		  }
	}
	
	public FactsStore unmarshallFacts(File file) {
		FactsStore store = null;
	    try {
	        JAXBContext context = JAXBContext.newInstance(FactsStore.class);
	        Unmarshaller unmarshaller = context.createUnmarshaller();
	        Object o = unmarshaller.unmarshal(file);
	        store = (FactsStore) o;
	        System.out.println("Objects created from XML:" + store.getName());
	        System.out.println("Objects created from XML:" + store.getPensions().size());
	        for (Client person : store.getClients()) {
	            System.out.println(person.getIPKcost() + " id= "+ person.getClientId());
	        }
	    } catch (JAXBException exception) {
	        Logger.getLogger(JAXBConverter.class.getName()).
	          log(Level.SEVERE, "marshallExample threw JAXBException", exception);
	    }
	    return store;
	}

}
