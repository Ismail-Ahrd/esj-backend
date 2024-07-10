package ma.inpt.esj.exception;

public class InvalidResetPasswordTokenException extends Exception{
    public InvalidResetPasswordTokenException(String message){
        super(message);
    }

    public InvalidResetPasswordTokenException(String message, Throwable throwable){
        super(message,throwable);
    }
}
