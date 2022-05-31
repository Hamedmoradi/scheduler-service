package ir.baam.exeption;

public enum SchedulerBusinessError {
    INVALID_INSTRUCTION_TYPE("so.business.exception.logic.invalid.instruction.type", "20005SO", 400),
    ACCOUNT_NUMBER_STRUCTURE_IS_WRONG("so.business.exception.logic.account.number.structure.is.wrong", "20006SO", 400),
    ILLEGAL_BMI_ACCOUNT_NUMBER_EXCEPTION("so.business.exception.logic.illegal.bmi.account.number", "20007SO", 400),
    STANDING_ORDER_NOT_FOUND("so.business.exception.logic.standing.order.not.found", "200035SO", 500);



    private String message;
    private String errorCode;
    private int status;

    SchedulerBusinessError(String message, String errorCode, int status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatus() {
        return status;
    }
}
