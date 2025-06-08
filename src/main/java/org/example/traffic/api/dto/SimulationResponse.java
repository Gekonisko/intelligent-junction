package org.example.traffic.api.dto;

import org.example.traffic.io.Command;
import java.util.List;

public class SimulationResponse {
    private List<Command> stepStatuses;

    public SimulationResponse() {
    }

    public SimulationResponse(List<Command> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }

    public List<Command> getStepStatuses() {
        return stepStatuses;
    }

    public void setStepStatuses(List<Command> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }
}

