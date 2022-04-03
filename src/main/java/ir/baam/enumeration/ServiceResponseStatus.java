package ir.baam.enumeration;

public enum ServiceResponseStatus {
    TIME_OUT("timeOut"),
    RUNTIME_EXCEPTION("runTimeException");

    private String ResponseValue;

    ServiceResponseStatus(String responseValue) {
        ResponseValue = responseValue;
    }

    public String getResponseValue() {
        return ResponseValue;
    }

    public ServiceResponseStatus getResponseServiceStatus(String response) {
        return ServiceResponseStatus.valueOf(response);
    }
}
