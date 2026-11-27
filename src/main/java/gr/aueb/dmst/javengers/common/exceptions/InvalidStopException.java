package gr.aueb.dmst.javengers.common.exceptions;

/* Exception thrown when someone requests an elevator floor that does not exist. */

public class InvalidStopException extends Exception {
   public InvalidStopException(String message) {
    super(message);
   } 
}
