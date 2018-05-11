package ru.ulpfr.pension_brms.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.ulpfr.pension_brms.managers.InputDataManager;

public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static class NATIONALITIES {
        public static final int NONE = 0;
        public static final int RUSSIA = 1;
        public static final int OTHER = 2;
    }
	public static class GENDER {
        public static final String FEMALE = "f";
        public static final String MALE = "m";
    }

    private Long clientId;		//номер в БЗ (ненерируется в ClientBuilder)
    private String name;		//ФИО 
    private Integer age; 		//возраст в годаx
    private String birthDate; 	// дата рождения
    private Integer nationality; //гражданство
    private String gender; 		//пол
    private Float IPK; 			//ИПК
    private Float IPK_cost;		//Стоимость ИПК
    
    //Стаж работы
    private Integer work_exp; //полныx лет
    private List<Object> work_exp_arr; // лет, месяцев, дней
    
    
    //все остальные свойства xранятся здесь
    private Map<String, Object> props;
    
    public Long getId() {
        return clientId;
    }

    public void setId(Long id) {
        this.clientId = id;
    }
    
    public Integer getAge() {
    	if(age == null)
    		age= calculateCurrentAge();
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String value) {
        this.birthDate = value;
    }
    
    public Map<String, Object> getProps() {
        return this.props;
    }

    public void setProps(Map<String, Object> properties) {
        this.props = properties;
    }
    
    public Object setProperty(String key, Object value) {
    	if(props == null)
    		props = new HashMap<String, Object>();
        return this.props.put(key, value);
    }

    public Object getProperty(String key) {
        return this.props.getOrDefault(key, null);
    }
    
	public int getNationality() {
		if(nationality == null)
			nationality = NATIONALITIES.NONE;
		return nationality;
	}

	public void setNationality(int nationality) {
		this.nationality = nationality;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public float getIPK() {
		return IPK;
	}

	public void setIPK(float ipk) {
		IPK = ipk;
	}

	public Float getIPKcost() {
		if(IPK_cost == null)
			IPK_cost = Float.parseFloat(Constants.getConstantValue("IPK_cost"));
		return IPK_cost;
	}

	public void setIPKcost(Float iPK_cost) {
		IPK_cost = iPK_cost;
	}

	public Integer getWorkExperience() {
		return work_exp;
	}

	public void setWorkExperience(Integer work_exp) {
		this.work_exp = work_exp;
	}

	public List<Object> getWorkExperienceArr() {
		return work_exp_arr;
	}

	public void setWorkExperienceArr(List<Object> work_exp_arr) {
		this.work_exp_arr = work_exp_arr;
	}
	

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        if (!Objects.equals(this.clientId, other.clientId)) {
            return false;
        }
        if (!Objects.equals(this.age, other.age)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Client [id = " + clientId + ", age=" + age + "]";
    }
    
    public int calculateCurrentAge()  {
    	if(birthDate != null) {
    		try {
    			String pattern  = "dd/MM/yyyy";
    			InputVariable conf = InputDataManager.getInstance().getInputVariableByName("D_Rogd");
    			if(conf != null && conf.getFormat() != null)
    				pattern = conf.getFormat();
    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
 			    LocalDate date = LocalDate.parse(birthDate, formatter);
 			   return Period.between(date, LocalDate.now()).getYears();
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
    	}
    	return 0;
    }
}
