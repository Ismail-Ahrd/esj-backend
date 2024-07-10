package ma.inpt.esj.services;

import ma.inpt.esj.exception.InvalidResetPasswordTokenException;
import ma.inpt.esj.exception.UserNotFoundException;

public interface PasswordResetService {
    void initiatePasswordReset(String email) throws UserNotFoundException;

    boolean validatePasswordResetToken(String token);

    void resetPassword(String token, String newPassword) throws InvalidResetPasswordTokenException;

}
