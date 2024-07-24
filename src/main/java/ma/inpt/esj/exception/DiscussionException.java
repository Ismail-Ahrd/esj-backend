package ma.inpt.esj.exception;

public class DiscussionException extends Exception {
    public DiscussionException(String message, Throwable cause) {super(message,cause);}

    public DiscussionException(String message) {
        super(message);
    }
}
