package ir.baam.enumeration;

public enum CommandEnumeration {
    INITIATE("initiate"),
    EXECUTE("execute"),
    RESCHEDULE_FOR_FAILED_INSTRUCTIONS("reschedule_For_Failed_Instructions"),
    RESCHEDULE_FOR_FAILED_TRANSACTIONS("reschedule_For_Failed_Transactions");

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
