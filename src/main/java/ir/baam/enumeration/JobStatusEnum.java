package ir.baam.enumeration;

public enum JobStatusEnum {
    PAUSED("PAUSED"),
    RESUMED("RESUMED"),
    SCHEDULED("SCHEDULED"),
    SCHEDULED_AND_STARTED("SCHEDULED & STARTED"),
    EDITED_AND_SCHEDULED("EDITED & SCHEDULED");

    private final String value;

    JobStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public JobStatusEnum getStatus(String status){
        return JobStatusEnum.valueOf(status);
    }
}
