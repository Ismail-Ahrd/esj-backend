package ma.inpt.esj.exception;


public class InvalidTokenException extends ConfirmationMailException {
    public InvalidTokenException() {
        super("Invalid confirmation token");
    }
}