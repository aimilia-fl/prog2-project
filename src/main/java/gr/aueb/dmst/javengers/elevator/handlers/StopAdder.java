package gr.aueb.dmst.javengers.elevator.handlers;

import gr.aueb.dmst.javengers.common.exceptions.InvalidStopException;
import gr.aueb.dmst.javengers.elevator.model.Stop;

/**
 * Adds a new stop to the elevator 
 */

public interface StopAdder {

    void addStop(Stop stop) throws InvalidStopException;
}