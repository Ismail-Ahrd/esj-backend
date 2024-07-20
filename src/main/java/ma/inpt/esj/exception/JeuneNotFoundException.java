package ma.inpt.esj.exception;

public class JeuneNotFoundException extends Exception {

    public JeuneNotFoundException() {
        super();
    }

    public JeuneNotFoundException(String message) {
        super(message);
    }

    public JeuneNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JeuneNotFoundException(Throwable cause) {
        super(cause);
    }
}