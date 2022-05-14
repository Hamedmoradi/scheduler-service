package ir.baam.enumeration;

import lombok.SneakyThrows;

public enum RecurringTransactionStatusEnum {

    START("start"),
    PENDING("pending"),
    PROCESSING("processing"),
    FINISH("finish");

    private String value;


    RecurringTransactionStatusEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @SneakyThrows
    public static RecurringTransactionStatusEnum from(final String value) {
        for (RecurringTransactionStatusEnum anEnum
                : RecurringTransactionStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        throw new Exception("Unknown value: " + value);
    }
}
