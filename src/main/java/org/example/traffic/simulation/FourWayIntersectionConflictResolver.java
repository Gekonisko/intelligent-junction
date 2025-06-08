package org.example.traffic.simulation;

import org.example.traffic.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class FourWayIntersectionConflictResolver implements IntersectionConflictResolver {

    public List<StepResult> nonConflictingGroup(List<Vehicle> vehicles, List<Pedestrian> pedestrians) {
        List<StepResult> groups = new ArrayList<>();
        int n = vehicles.size();
        int max = 1 << n;

        for (int mask = 1; mask < max; mask++) {
            StepResult group = new StepResult();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    group.addVehicle(vehicles.get(i));
                }
            }
            if (!isConflict(group)) {
                groups.add(group);
                group.addPedestrians(getNoConflictPedestrians(group.getLeftVehicles(), pedestrians));
            }
        }
        return groups;
    }

    public boolean isConflict(Vehicle a, Vehicle b) {
        Movement aMove = a.getMovement();
        Movement bMove = b.getMovement();

        if(aMove == null || bMove == null) return true;
        if(a.from == b.from) return true;

        if(aMove == Movement.STRAIGHT)
        {
            if (Direction.oppositeOf(a.from) == b.from &&
                    (bMove == Movement.STRAIGHT || bMove == Movement.RIGHT)) return false;
            else if(Direction.leftOf(a.from) == b.from &&
                    bMove == Movement.RIGHT) return false;
        }
        else if(aMove == Movement.RIGHT)
        {
            if(Direction.oppositeOf(a.from) == b.from &&
                    (bMove == Movement.STRAIGHT || bMove == Movement.RIGHT)) return false;
            else if(Direction.leftOf(a.from) == b.from &&
                    (bMove == Movement.RIGHT || bMove == Movement.LEFT)) return false;
            else if(Direction.rightOf(a.from) == b.from &&
                    (bMove == Movement.STRAIGHT || bMove == Movement.RIGHT  || bMove == Movement.LEFT)) return false;
        }
        else if(aMove == Movement.LEFT)
        {
            if(Direction.oppositeOf(a.from) == b.from &&
                    bMove == Movement.LEFT) return false;
            else if(Direction.leftOf(a.from) == b.from &&
                    bMove == Movement.RIGHT) return false;
            else if(Direction.rightOf(a.from) == b.from &&
                    bMove == Movement.RIGHT) return false;
        }

        return true;

    }

    private boolean isConflict(StepResult group) {
        for (int i = 0; i < group.getLeftVehicles().size(); i++) {
            for (int j = i + 1; j < group.getLeftVehicles().size(); j++) {
                if (isConflict(group.getLeftVehicles().get(i), group.getLeftVehicles().get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Pedestrian> getNoConflictPedestrians(List<Vehicle> vehicles, List<Pedestrian> pedestrians) {
        List<Direction> targetDirections = new ArrayList<>();
        for (Vehicle v : vehicles) {
            targetDirections.add(v.to);
        }

        List<Pedestrian> noConflictPedestrians = new ArrayList<>();
        for (Pedestrian p : pedestrians) {
            if (!targetDirections.contains(p.direction)) {
                noConflictPedestrians.add(p);
            }
        }
        return noConflictPedestrians;
    }

}
