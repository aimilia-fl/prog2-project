package gr.aueb.dmst.javengers.ui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.aueb.dmst.javengers.common.dto.ElevatorResponse;
import gr.aueb.dmst.javengers.common.dto.PassengerRequest;
import gr.aueb.dmst.javengers.common.exceptions.BadRequestException;
import gr.aueb.dmst.javengers.common.exceptions.InvalidMethodException;
import gr.aueb.dmst.javengers.common.exceptions.MissingSourceFloorException;

public class UserInterface {

  private final int sourceFloor;
  private final String URL = "http://localhost:8080/api/v1/central/requests";
  private final HttpClient client = HttpClient.newHttpClient();
  private final ObjectMapper mapper = new ObjectMapper();

  public UserInterface(int sourceFloor) {
    this.sourceFloor = sourceFloor;
  }

  protected String sendRequest(int destinationFloor) {

    try {
      PassengerRequest passengerRequest =
          new PassengerRequest(sourceFloor, destinationFloor);
      String jsonBody = mapper.writeValueAsString(passengerRequest);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
          .build();

      HttpResponse<String> response =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      switch (response.statusCode()) {
        case 200 -> {
          ElevatorResponse elevatorResponse =
              mapper.readValue(response.body(), ElevatorResponse.class);
          return "Assigned Elevator: " + elevatorResponse.getAssignedElevator()
              + "\nGo to Elevator No: " + elevatorResponse.getAssignedElevator();
        }
        case 400 -> {
          String serverError = mapper.readTree(response.body())
              .path("error").asText("Invalid Request");
          throw new BadRequestException("Please try again. " + serverError);
        }
        case 405 -> throw new InvalidMethodException(
            "API Error. Method should be POST");
        default -> {
          return "Operation Completed and Exited with Status Code: "
              + response.statusCode();
        }
      }
    } catch (BadRequestException | InvalidMethodException e) {
      return e.getMessage();
    } catch (Exception e) {
      return "Connection Failed";
    }
  }

  public static void main(String[] args) throws MissingSourceFloorException {

    int sourceFloor;
    if (args.length == 0) {
      throw new MissingSourceFloorException(
          "Source floor is missing. Please provide a floor number.");
    }

    try {
      sourceFloor = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.err.println(
          "Invalid source floor. Please enter a valid integer.");
      return;
    }

    if (sourceFloor < 0 || sourceFloor > 200) {
      System.err.println(
          "Invalid source floor. Please select a floor between 0 and 200.");
      return;
    }

    System.out.println("\n============== Elevator Panel ==============");
    Scanner sc = new Scanner(System.in);
    UserInterface terminal = new UserInterface(sourceFloor);

    while (true) {
      System.out.println(
          "Please select your destination floor or \"exit\" to quit");
      String input = sc.nextLine().trim();

      if ("exit".equalsIgnoreCase(input)) {
        break;
      }

      try {
        int destinationFloor = Integer.parseInt(input);

        if (destinationFloor < 0 || destinationFloor > 200) {
          System.out.println(
              "Invalid floor. Please select a floor between 0 and 200.");
          continue;
        }

        if (destinationFloor == sourceFloor) {
          System.out.println(
              "You are already on this floor. Please select another floor.");
          continue;
        }

        String result = terminal.sendRequest(destinationFloor);

        if ("Connection Failed".equals(result)) {
          System.out.println(
              "Connection failed. Please check that the central server is running.");
        } else {
          System.out.println(result);
        }

      } catch (NumberFormatException e) {
        System.err.println("Invalid Input. Please try again");
      }
    }
  }
}

