package gr.aueb.dmst.javengers.central.dispatch;

import java.util.Map;
import java.util.Optional;

import gr.aueb.dmst.javengers.common.dto.ElevatorStatus;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;


/**
 * Strategy interface defining the method of elevator selection
 * to serve a request.
 */
public interface DispatchStrategy {

    /**
     * Evaluates the current state of the elevator fleet and selects the most
     * suitable elevator for the incoming passenger request.
     *
     * @param request       the passenger trip request containing source and destination floors
     * @param fleetStatuses a map pairing each active elevator's ID (e.g., {@code "A"})
     *                      with its latest {@link ElevatorStatus}
     * 
     * 
     * 
     * @return an {@link Optional} containing the ID of the selected elevator, or
     *         {@link Optional#empty()} if no operational elevator can serve the request
     */
    Optional<String> selectElevator(PassengerRequest request, Map<String, ElevatorStatus> fleetStatuses);
}