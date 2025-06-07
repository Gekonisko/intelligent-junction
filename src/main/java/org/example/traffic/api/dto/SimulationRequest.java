package org.example.traffic.api.dto;

import org.example.traffic.io.Command;

import java.util.List;

public class SimulationRequest {
    private List<Command> commands;

    public SimulationRequest() {
    }

    public SimulationRequest(List<Command> commands) {
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}