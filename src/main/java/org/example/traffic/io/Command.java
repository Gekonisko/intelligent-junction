package org.example.traffic.io;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddVehicleCommand.class, name = "addVehicle"),
        @JsonSubTypes.Type(value = StepCommand.class, name = "step"),
        @JsonSubTypes.Type(value = FailureModeCommand.class, name = "failureMode"),
        @JsonSubTypes.Type(value = RoadPriorityCommand.class, name = "roadPriority"),
        @JsonSubTypes.Type(value = AddPedestrianCommand.class, name = "addPedestrian")
})
public abstract class Command {
    public String type;

    protected Command(String type) {
        this.type = type;
    }
}