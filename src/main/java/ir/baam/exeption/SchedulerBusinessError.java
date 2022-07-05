package ir.baam.exeption;

public enum SchedulerBusinessError {
    STANDING_ORDER_NOT_FOUND("so.business.exception.logic.standing.order.not.found", "200035SO", 500),
    INVALID_COMMAND("so.business.exception.logic.invalid.command", "200032SO", 500);



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
