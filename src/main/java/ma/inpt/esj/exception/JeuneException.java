package ma.inpt.esj.exception;

public class JeuneException extends RuntimeException {

    public JeuneException() {
        super();
    }

    public JeuneException(String message) {
        super(message);
    }

    public JeuneException(String message, Throwable cause) {
        super(message, cause);
    }

    public JeuneException(Throwable cause) {
        super(cause);
    }
}

