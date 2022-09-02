package ir.baam.enumeration;

public enum CommandEnumeration {
    PREPARE_STANDING_ORDER_SERVICE("PREPARE_STANDING_ORDER_SERVICE"),
    INITIATE("INITIATE"),
    EXECUTE("EXECUTE"),
    COMPLETED_IN_THIS_PERIOD("COMPLETED_IN_THIS_PERIOD"),
    TERMINATE_IN_THIS_PERIOD("TERMINATE_IN_THIS_PERIOD"),
    INITIATION_FAILED("INITIATION_FAILED"),
    EXECUTION_FAILED("EXECUTION_FAILED");

    private final String value;

    CommandEnumeration(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public CommandEnumeration getJobServices(String command) {
        return CommandEnumeration.valueOf(command);
    }
}
