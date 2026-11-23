package gr.aueb.dmst.javengers.elevator.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ElevatorConfigTest {
    @Test 
    public void testGetData() {
        ElevatorConfig elevatorConfig = new ElevatorConfig(0, 200, 5.00, 800.00);
        assertEquals(0, elevatorConfig.getMinFloor());
        assertEquals(200, elevatorConfig.getMaxFloor());
        assertEquals(5.00, elevatorConfig.getSpeed(), 0.001);
        assertEquals(800.00, elevatorConfig.getMaxWeight(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFloors() {
        new ElevatorConfig(200, 0, 5.00, 800.00);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSpeed() {
        new ElevatorConfig(0, 200, -5.00, 800.00);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWeight() {
        new ElevatorConfig(0,200,5.00,0);
    }
}
