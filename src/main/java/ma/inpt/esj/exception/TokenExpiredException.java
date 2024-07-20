package ma.inpt.esj.exception;

public class TokenExpiredException extends ConfirmationMailException {
    public TokenExpiredException() {
        super("Confirmation token has expired");
    }
}
