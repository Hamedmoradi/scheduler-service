package ir.baam.enumeration;

import lombok.SneakyThrows;

public enum StandingOrderTransactionStatusEnum {

    START("START"),
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    FINISH("FINISH"),
    FAILED("FAILED"),
    INITIATION_FAILED("INITIATION_FAILED"),
    EXECUTION_FAILED("EXECUTION_FAILED"),
    INITIATION_CONNECTION_FAILED("INITIATION_CONNECTION_FAILED"),
    EXECUTION_CONNECTION_FAILED("EXECUTION_CONNECTION_FAILED"),
    TERMINATED("TERMINATED"),
    SUCCEED("SUCCEED"),
    INITIATE("INITIATE"),
    ACTIVE("ACTIVE"),
    SUSPEND("SUSPEND");

    private String value;


    StandingOrderTransactionStatusEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @SneakyThrows
    public static StandingOrderTransactionStatusEnum from(final String value) {
        for (StandingOrderTransactionStatusEnum anEnum : StandingOrderTransactionStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        throw new Exception("Unknown value: " + value);
    }
}
