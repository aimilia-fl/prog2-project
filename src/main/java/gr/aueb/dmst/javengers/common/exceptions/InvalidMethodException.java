package gr.aueb.dmst.javengers.common.exceptions;

/*

Exception for incorrect methods (GET/PUT/POST)
Status Code: 405

*/

public class InvalidMethodException extends Exception {
    public InvalidMethodException(String message){
        super(message);
    }
}