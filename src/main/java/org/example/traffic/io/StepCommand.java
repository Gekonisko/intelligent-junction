package org.example.traffic.io;

import org.example.traffic.model.StepStatus;

public class StepCommand extends Command {
    private StepStatus stepResult;

    public StepCommand() {
        super("step");
    }

    public StepStatus getStepResult() {
        return stepResult;
    }

    public void setStepResult(StepStatus stepResult) {
        this.stepResult = stepResult;
    }
}