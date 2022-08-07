package ir.baam.enumeration;

import lombok.SneakyThrows;

public enum StandingOrderTransactionStatusEnum {

    REQUESTED("REQUESTED"),
    PROPOSED("PROPOSED"),
    OFFERED("OFFERED"),
    CANCELED("CANCELED"),
    FULL_FILLED("FULL_FILLED"),
    TERMINATED("TERMINATED"),
    COMPLETED("COMPLETED"),
    DELETED("DELETED"),
    PENDING("PENDING"),
    SUCCEEDED("SUCCEEDED"),
    FAILED("FAILED"),
    SUSPENDED("SUSPENDED");

    private String value;


    StandingOrderTransactionStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @SneakyThrows
    public static StandingOrderTransactionStatusEnum from(final String value) {
        for (StandingOrderTransactionStatusEnum anEnum
                : StandingOrderTransactionStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        throw new Exception("Unknown value: " + value);
    }
}
