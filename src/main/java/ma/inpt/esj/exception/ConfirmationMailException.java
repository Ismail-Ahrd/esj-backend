package ma.inpt.esj.exception;

public class ConfirmationMailException extends RuntimeException{
    public ConfirmationMailException(String message){
        super(message);
    }

    public ConfirmationMailException(String message, Throwable throwable){
        super(message,throwable);
    }
}
