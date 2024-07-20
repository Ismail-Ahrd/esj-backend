package ma.inpt.esj.exception;

public class ProfessionnelSanteNotFoundException extends  RuntimeException{
    public ProfessionnelSanteNotFoundException(String message) {
        super(message);
    }

    public ProfessionnelSanteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
