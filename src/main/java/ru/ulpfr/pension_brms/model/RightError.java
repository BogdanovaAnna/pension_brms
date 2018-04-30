package ru.ulpfr.pension_brms.model;

public class RightError {
	
	public static enum ERROR_TYPES {
        COMMON, // общие
        PENS_AGE, //по пенсии по старости
        PENS_DISABILITY, // по пенсии по инвалидности
        PENS_LOSS // по пенсии по поере кормильца
    }

	private ERROR_TYPES errorCode;
	private long clientId;
	private String errorMessage;

	public RightError(String msg) {
		errorMessage = msg;
		errorCode = ERROR_TYPES.COMMON;
	}
	
	public RightError(String msg, ERROR_TYPES code, long client_id) {
		errorMessage = msg;
		errorCode = code;
		clientId = client_id;
	}
	
	public RightError(String msg, ERROR_TYPES code) {
		errorMessage = msg;
		errorCode = code;
	}

	public ERROR_TYPES getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ERROR_TYPES errorCode) {
		this.errorCode = errorCode;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
