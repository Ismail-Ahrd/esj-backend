package ma.inpt.esj.exception;

public class ProfessionnelNotFoundException extends Exception{
    public ProfessionnelNotFoundException(String message) {
        super(message);
    }

    public ProfessionnelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
