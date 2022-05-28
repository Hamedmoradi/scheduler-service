package ir.baam.enumeration;

import lombok.SneakyThrows;

public enum StandingOrderTransactionStatusEnum {

    START("start"),
    PENDING("pending"),
    PROCESSING("processing"),
    FINISH("finish"),
    INITIATION_FAILED("initiation_failed"),
    FAILED("failed"),
    INITIATION_CONNECTION_FAILED("initiation_connection_failed"),
    EXECUTION_CONNECTION_FAILED("execution_connection_failed"),
    SUCCEED("succeed");

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
