package com.example.dbseparation.exception;


public class CustomRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 6733191316650283806L;

    private final String hospitalCd;

    public CustomRuntimeException(String message) {
        super(message);
        this.hospitalCd = null;
    }

    public CustomRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.hospitalCd = null;
    }

    public CustomRuntimeException(String hospitalCd, String message) {
        super(message);
        this.hospitalCd = hospitalCd;
    }

    public CustomRuntimeException(String hospitalCd, String message, Throwable cause) {
        super(message, cause);
        this.hospitalCd = hospitalCd;
    }

    public CustomRuntimeException(Throwable cause, String hospitalCd) {
        super(cause);
        this.hospitalCd = hospitalCd;
    }

    public String getHospitalCd() {
        return hospitalCd;
    }
}
