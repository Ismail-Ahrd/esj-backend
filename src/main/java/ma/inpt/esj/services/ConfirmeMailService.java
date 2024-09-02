package ma.inpt.esj.services;


import ma.inpt.esj.exception.*;

public interface ConfirmeMailService {
    void sendEmail(String to, String subject, String body);
    Object confirmEmail(String token) throws ConfirmationMailException;

     void sendConfirmationEmail(String to, String token);

     void resendToken(String email) throws MedecinNotFoundException,AdministrateurNotFoundException, UserNotFoundException, JeuneNotFoundException, ProfessionnelNotFoundException;
}
