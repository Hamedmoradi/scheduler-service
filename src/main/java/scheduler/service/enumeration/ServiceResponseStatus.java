package scheduler.service.enumeration;

public enum ServiceResponseStatus {
    TIME_OUT("timeOut"),
    RUNTIME_EXCEPTION("runTimeException");

    private final String ResponseValue;

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
