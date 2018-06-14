package ru.ulpfr.pension_brms.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ru.ulpfr.pension_brms.model.rules.*;

@XmlRootElement(name = "payload")
public class FactsStore {
	@XmlElement(name="client")
	private List<Client> client = new ArrayList<Client>();
	@XmlElement(name="pension")
	private List<Pension> pension = new ArrayList<Pension>();
	private String name;
	
	public List<Client> getClients() {
        return client;
    }
 
    public void setClients(List<Client> _clients) {
        this.client = _clients;
    }
    
    public void setPensions(List<Pension> _pensions) {
        this.pension = _pensions;
    }
    
    public List<Pension> getPensions() {
        return pension;
    }
    
    public void setName(String _name) {
        this.name = _name;
    }
    
    public String getName() {
        return this.name;
    }
	
}
