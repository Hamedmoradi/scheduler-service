package ir.baam.enumeration;

public enum CommandEnumeration {
    INITIATE("INITIATE"),
    EXECUTE("EXECUTE"),
    FAILED_INSTRUCTIONS("FAILED_INSTRUCTIONS"),
    FAILED_TRANSACTIONS("FAILED_TRANSACTIONS");

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
