package org.example.traffic.io;

public class FailureModeCommand extends Command  {
    public boolean state;

    public FailureModeCommand() {
        super("failureMode");
    }

    public FailureModeCommand(boolean state) {
        super("failureMode");
        this.state = state;
    }
}
