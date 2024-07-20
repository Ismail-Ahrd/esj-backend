package ma.inpt.esj.exception;

public class PhoneNonValideException extends Exception {

    public PhoneNonValideException() {super();}

    public PhoneNonValideException(String message) {
        super(message);
    }

    public PhoneNonValideException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNonValideException(Throwable cause) {
        super(cause);
    }
}
