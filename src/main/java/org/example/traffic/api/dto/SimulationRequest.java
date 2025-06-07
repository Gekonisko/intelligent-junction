package org.example.traffic.api.dto;

import org.example.traffic.io.Command;

import java.util.List;

public class SimulationRequest {
    private List<Command> commands;

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}