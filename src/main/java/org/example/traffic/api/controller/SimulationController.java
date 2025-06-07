package org.example.traffic.api.controller;

import org.example.traffic.api.dto.SimulationRequest;
import org.example.traffic.api.dto.SimulationResponse;
import org.example.traffic.io.Command;
import org.example.traffic.io.StepCommand;
import org.example.traffic.model.EngineType;
import org.example.traffic.model.StepStatus;
import org.example.traffic.simulation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    @PostMapping
    public ResponseEntity<SimulationResponse> runSimulation(@RequestBody SimulationRequest request) {
        IntersectionConflictResolver conflictResolver = new FourWayIntersectionConflictResolver();
        FourWayIntersection intersection = new FourWayIntersection(conflictResolver);
        IntersectionDecisionTree decisionTree = new IntersectionDecisionTree(intersection, conflictResolver, 4, 4);
        SimulationEngine engine = new SimulationEngine(intersection, EngineType.DECISION_TREE, decisionTree);
        List<StepStatus> statuses = engine.runSimulation(request.getCommands());

        var result = mapper(request.getCommands(), statuses);

        return ResponseEntity.ok(new SimulationResponse(result));
    }

    private List<Command> mapper(List<Command> commands, List<StepStatus> stepStatuses)
    {
        int stepIndex = 0;
        for (Command cmd : commands) {
            if (cmd instanceof StepCommand && stepIndex < stepStatuses.size()) {
                ((StepCommand) cmd).setStepResult(stepStatuses.get(stepIndex));
                stepIndex++;
            }
        }

        return commands;
    }
}
