package gr.aueb.dmst.javengers.elevator.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import gr.aueb.dmst.javengers.common.exceptions.InvalidStopException;

public class ElevatorTest {
    @Test
    public void testElevator() {
        ElevatorConfig config = new ElevatorConfig(3, 200, 3, 800);
        Elevator elevator = new Elevator("B", 15, config);

        assertEquals("B", elevator.getElevatorId());
        assertEquals(15, elevator.getCurrentFloor());
        assertEquals(Direction.IDLE, elevator.getDirection());
        assertEquals(DoorState.CLOSED, elevator.getDoorState());
        assertEquals(OperatingState.NORMAL, elevator.getOperatingState());
        assertEquals(0, elevator.getRemainingStops());
    }

    @Test
    public void testInvalidElevator() {
        ElevatorConfig config = new ElevatorConfig(3, 200, 3, 800);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Elevator(null, 15, config);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Elevator("", 15, config);
        });
    }

    @Test
    public void testInvalidConfig() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Elevator("B", 0, null);
        });
    }

    @Test
    public void testInvalidCurrentFloor() {
        ElevatorConfig config = new ElevatorConfig(3, 200, 3, 800);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Elevator("B", -1, config);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Elevator("B", 203, config);
        });
    }

    @Test
    public void testAddValidStop() throws InvalidStopException {
        ElevatorConfig config = new ElevatorConfig(3, 200, 3, 800);
        Elevator elevator = new Elevator("B", 15, config);

        Stop stop = new Stop(7,StopType.DROP_OFF);

        elevator.addStop(stop);
        assertEquals(1, elevator.getRemainingStops());
    }

    @Test
    public void testAddNullStop() {
        ElevatorConfig config = new ElevatorConfig(3, 200, 3, 800);
        Elevator elevator = new Elevator("B", 15, config);

        assertThrows(IllegalArgumentException.class, () -> {
           elevator.addStop(null);
        });
    }

    @Test
    public void testAddInvalidFloor() {
        ElevatorConfig config = new ElevatorConfig(3, 200, 3, 800);
        Elevator elevator = new Elevator("B", 15, config);

        Stop stop = new Stop(250, StopType.PICK_UP);

        assertThrows(InvalidStopException.class, () -> {
            elevator.addStop(stop);
        });
    }
}
