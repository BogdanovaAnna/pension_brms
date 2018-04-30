package ru.ulpfr.pension_brms.utils;

import java.util.ArrayList;
import java.util.Arrays;

import ru.ulpfr.pension_brms.model.Client;

public class ClientBuilder {

	private final Client instance;
    private static Long clientIdGenerator = 1L;
    
    public ClientBuilder() {
        instance = new Client();
        instance.setId(clientIdGenerator++);
    }
    
    public ClientBuilder withId(Long id){
        instance.setId(id);
        return this;
    }
    
    public ClientBuilder withName(String name){
        instance.setName(name);
        return this;
    }
    
    public ClientBuilder withAge(int age){
        instance.setAge(age);
        return this;
    }
    
    public ClientBuilder withDateBirth(String date){
        instance.setBirthDate(date);
        return this;
    }
    
    public ClientBuilder withGender(String g){
        instance.setGender(g);
        return this;
    }
    
    public ClientBuilder withProperty(String key, Object value){
        instance.setProperty(key, value);
        return this;
    }
    
    public ClientBuilder withNationality(int n){
        instance.setNationality(n);
        return this;
    }
   
    public ClientBuilder withIPK(Float value){
        instance.setIPK(value);
        return this;
    }
    
    public ClientBuilder withIPKCost(Float value){
        instance.setIPKcost(value);
        return this;
    }
    
    public ClientBuilder withWorkExperience(Object value){
    	try {
			instance.setWorkExperience(Integer.parseInt(value.toString()));
		} catch (Exception e) {
			ArrayList<Object> arr = new ArrayList<>(Arrays.asList(value));
			instance.setWorkExperienceArr(arr);
			instance.setWorkExperience(Integer.parseInt(arr.get(0).toString()));
		}
        return this;
    }
    
    public Client build(){
        return instance;
    }

    public ClientBuilder end() {
        return this;
    }

}
