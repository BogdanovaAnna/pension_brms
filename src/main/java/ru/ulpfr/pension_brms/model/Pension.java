package ru.ulpfr.pension_brms.model;

public class Pension {
	public static class PENS_TYPE {
        public static final int Old_age = 1;
        public static final int Disability = 2;
        public static final int CGR = 3;
    }
	
	public static class PENS_STATUS {
		public static final int NA = 0;
		public static final int REJECTED = 1;
        public static final int ASSIGNED = 2;
    }
	
	private int pensType; 	// тип пенсии: по возрасту, инвалидность, СПК
	private long clientId; 	//айди гражданина
	private Float amount; 	//размер пенсии
	private int status; 	//статус: назначена, не назначена

	public Pension(long id, Integer type) {
		this.clientId = id;
		this.pensType = type;
		this.status = PENS_STATUS.NA;
	}

	public int getPensType() {
		return pensType;
	}

	public void setPensType(int id) {
		this.pensType = id;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public void setAmount(Integer amount) {
		this.amount = Float.valueOf(amount);
	}
	public void setAmount(double amount) {
		this.amount = (float)amount;
	}
	
	

}
