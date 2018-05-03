package ru.ulpfr.pension_brms.model;

import ru.ulpfr.pension_brms.model.Pension.PENS_TYPE;

public class Right {
	
	private static long idCounter;
	private long rightId;
	private long clientId;
	private int pensType;
	private int type  = RIGHT_TYPES.NONE;
	
	public static class RIGHT_TYPES {
        public static final int NONE = 0;
        public static final int NATIONALITY = 1;
        public static final int AGE = 2;
        public static final int MIN_EXP = 3;
        public static final int MIN_IPK = 4;
    }

	public Right(long cl, Integer tp) {
		this.setClientId(cl);
		this.setType(tp);
		this.setPensType(PENS_TYPE.Old_age);
		this.setRightId(++idCounter);
	}
	
	public Right(long cl, Integer tp, Integer pt) {
		this.setClientId(cl);
		this.setType(tp);
		this.setPensType(pt);
		this.setRightId(++idCounter);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long client) {
		this.clientId = client;
	}

	public long getRightId() {
		return rightId;
	}

	public void setRightId(long rightId) {
		this.rightId = rightId;
	}

	public int getPensType() {
		return pensType;
	}

	public void setPensType(int pensType) {
		this.pensType = pensType;
	}

}
