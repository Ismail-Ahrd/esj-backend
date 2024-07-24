package ma.inpt.esj.services;


import lombok.AllArgsConstructor;
import ma.inpt.esj.entities.InfoUser;
import ma.inpt.esj.entities.PasswordResetToken;
import ma.inpt.esj.exception.InvalidResetPasswordTokenException;
import ma.inpt.esj.exception.UserNotFoundException;
import ma.inpt.esj.repositories.InfoUserRepository;
import ma.inpt.esj.repositories.PasswordResetTokenRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private InfoUserRepository userRepository;

    private PasswordResetTokenRepository tokenRepository;

    private JavaMailSender mailSender;
    private PasswordEncoder passwordEncoder;

    @Override
    public void initiatePasswordReset(String email) throws UserNotFoundException {
        Optional<InfoUser> userOptional = userRepository.findByMail(email);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        InfoUser user = userOptional.get();
        String token = UUID.randomUUID().toString().replace("-", "").substring(0,6);

        Date expiryDate = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour expiry
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, expiryDate);
        tokenRepository.save(passwordResetToken);

        sendPasswordResetEmail(user.getMail(), token);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        return resetToken != null && resetToken.getExpiryDate().after(new Date());
    }

    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) throws InvalidResetPasswordTokenException {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
            throw new InvalidResetPasswordTokenException("Invalid or expired token");
        }
        InfoUser user = resetToken.getUser();
        user.setMotDePasse(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void sendPasswordResetEmail(String recipientEmail, String token){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Réinitialisation de mot de passe");
        mailMessage.setText("Pour réinitialiser votre mot de passe, utilisé le token suviant  : "+token);

        mailSender.send(mailMessage);

    }
}
