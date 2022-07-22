package ir.baam.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a schedule identifier for the user interface representing both JobIdentifier and TriggerIdentifiers
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleIdentifier {

    private String name;
    private String group = "DEFAULT";

    public ScheduleIdentifier() {
        super();
    }

    public ScheduleIdentifier(@JsonProperty("name") String name, @JsonProperty("group") String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}



 
