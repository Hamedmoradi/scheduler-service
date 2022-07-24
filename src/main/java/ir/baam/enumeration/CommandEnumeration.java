package ir.baam.enumeration;

public enum CommandEnumeration {
    INITIATE("INITIATE"),
    EXECUTE("EXECUTE"),
    INITIATION_FAILED("INITIATION_FAILED"),
    EXECUTION_FAILED("EXECUTION_FAILED");

    private String value;

    CommandEnumeration(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public CommandEnumeration getJobServices(String command){
        return CommandEnumeration.valueOf(command);
    }
}
