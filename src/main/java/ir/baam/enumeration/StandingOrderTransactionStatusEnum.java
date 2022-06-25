package ir.baam.enumeration;

import lombok.SneakyThrows;

public enum StandingOrderTransactionStatusEnum {

    START("start"),
    PENDING("pending"),
    PROCESSING("processing"),
    FINISH("finish"),
    FAILED("failed"),
    INITIATION_FAILED("initiation_failed"),
    EXECUTION_FAILED("execution_failed"),
    INITIATION_CONNECTION_FAILED("initiation_connection_failed"),
    EXECUTION_CONNECTION_FAILED("execution_connection_failed"),
    TERMINATED("terminated"),
    SUCCEED("succeed"),
    INITIALIZED("initialized"),
    ACTIVE("active"),
    SUSPEND("suspend");

    private String value;


    StandingOrderTransactionStatusEnum(String value) {
        this.value = value;
    }

    public String value() {
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