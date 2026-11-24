package gr.aueb.dmst.javengers.elevator.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StopTest {
    @Test
    public void testGetTargetFloor() {
        Stop stop = new Stop(7, StopType.PICK_UP);
        assertEquals(7,stop.getTargetFloor());
    }

    @Test 
    public void testGetStopType() {
        Stop stop = new Stop(7, StopType.PICK_UP);
        assertEquals(StopType.PICK_UP,stop.getStopType());
    }
}