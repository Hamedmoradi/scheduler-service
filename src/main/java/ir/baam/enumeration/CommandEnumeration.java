package ir.baam.enumeration;

public enum CommandEnumeration {
    INITIATE("INITIATE"),
    EXECUTE("EXECUTE"),
    RESCHEDULE_FOR_FAILED_INSTRUCTIONS("RESCHEDULE_FOR_FAILED_INSTRUCTIONS"),
    RESCHEDULE_FOR_FAILED_TRANSACTIONS("RESCHEDULE_FOR_FAILED_TRANSACTIONS");

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
