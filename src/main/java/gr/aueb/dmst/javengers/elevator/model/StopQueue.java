package gr.aueb.dmst.javengers.elevator.model;

import java.util.ArrayList;

public class StopQueue {

    private ArrayList<Stop> queue = new ArrayList<>();
    
    public void addStop(Stop elevStop) {
        for (int i = 0;  i < queue.size(); i++) {
            Stop currentStop = queue.get(i);
            if (currentStop.getTargetFloor() == elevStop.getTargetFloor() && currentStop.getStopType() == elevStop.getStopType()) {
                return;
            }
        }
        queue.add(elevStop);
    }

    public Stop getNexStop() {
        if (queue.size() == 0) {
            return null;
        }
        return queue.get(0);
    }

    public void removeNextStop() {
        if (queue.size() > 0) {
            queue.remove(0);
        }
    }

    public int remainingStops() {
        return queue.size();
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
}
