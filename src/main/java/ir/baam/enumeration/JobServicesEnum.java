package ir.baam.enumeration;

public enum JobServicesEnum {
    FUTURE("FUTURE"),
    RECURRING("RECURRING"),
    BATCH("BATCH"),
    INDIRECT_BATCH("INDIRECT_BATCH");

    private final String value;

    JobServicesEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public JobStatusEnum getJobServices(String status){
        return JobStatusEnum.valueOf(status);
    }
}
