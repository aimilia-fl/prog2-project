package gr.aueb.dmst.javengers.common.exceptions;

/*

Exception for bad requests. For example floor < 0, floor > 20, srcFloor=destinationFloor etc
Used in UserInterface.java in sendRequest method
Status Code: 400

*/
public class BadRequestException extends Exception {

    public BadRequestException(String message){
        super(message);
    }

}