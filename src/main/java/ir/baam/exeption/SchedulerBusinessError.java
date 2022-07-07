package ir.baam.exeption;

public enum SchedulerBusinessError {
    STANDING_ORDER_NOT_FOUND("ss.business.exception.logic.standing.order.not.found", "300001SS", 400),
    STANDING_ORDER_INTERNAL_ERROR("ss.business.exception.logic.standing.order.internal.error", "300002SS", 500),
    INVALID_COMMAND("ss.business.exception.logic.invalid.command", "300003SS", 400),
    FILL_MANDATORY_PARAMETERS("ss.business.exception.fill.mandatory.parameters","300004SS" , 400),
    GENERAL_MISMATCH_INPUT("ss.business.exception.general.mismatch.input","300005SS" , 400),
    RESOURCE_NOT_FOUND("ss.business.exception.resource.not.found","300005SS" , 400),
    SCHEDULER_JSON_STRUCTURE_IS_INVALID("ss.business.exception.json.structure.is.invalid","300006SS" , 400);



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
