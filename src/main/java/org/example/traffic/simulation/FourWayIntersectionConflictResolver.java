package org.example.traffic.simulation;

import org.example.traffic.model.Direction;
import org.example.traffic.model.Movement;
import org.example.traffic.model.Vehicle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FourWayIntersectionConflictResolver implements IntersectionConflictResolver {

    public Set<List<Vehicle>> nonConflictingGroup(List<Vehicle> vehicles) {
        Set<List<Vehicle>> groups = new HashSet<>();
        int n = vehicles.size();
        int max = 1 << n;

        for (int mask = 1; mask < max; mask++) {
            List<Vehicle> group = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    group.add(vehicles.get(i));
                }
            }
            if (!isConflict(group)) {
                groups.add(group);
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
                    bMove == Movement.RIGHT) return false;
            else if(Direction.rightOf(a.from) == b.from &&
                    (bMove == Movement.STRAIGHT || bMove == Movement.RIGHT)) return false;
        }
        else if(aMove == Movement.LEFT)
        {
            if(Direction.leftOf(a.from) == b.from &&
                    bMove == Movement.RIGHT) return false;
        }

        return true;

    }

    private boolean isConflict(List<Vehicle> group) {
        for (int i = 0; i < group.size(); i++) {
            for (int j = i + 1; j < group.size(); j++) {
                if (isConflict(group.get(i), group.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

}
