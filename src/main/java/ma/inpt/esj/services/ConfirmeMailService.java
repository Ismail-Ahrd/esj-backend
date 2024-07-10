package ma.inpt.esj.services;

public interface ConfirmeMailService<T> {
    void sendEmail(String to, String subject, String body);
    T confirmEmail(String token);

    default void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/medecins/confirmation?token=" + token;
        String subject = "Email Confirmation";
        String htmlBody = "<p>Please confirm your email by clicking the following link:</p>"
                + "<p><a href=\"" + confirmationUrl + "\">Confirm Email</a></p>";

        sendEmail(to, subject, htmlBody);
    }
}
