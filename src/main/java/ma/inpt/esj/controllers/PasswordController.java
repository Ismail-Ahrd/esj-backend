package ma.inpt.esj.controllers;


import lombok.AllArgsConstructor;
import ma.inpt.esj.exception.InvalidResetPasswordTokenException;
import ma.inpt.esj.exception.UserNotFoundException;
import ma.inpt.esj.services.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/password")
@AllArgsConstructor
public class PasswordController {

    private PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request){
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        try{
            passwordResetService.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset link has been sent to your email");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email does not exist");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request){
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Token and new password are required");
        }
        try{
            if(passwordResetService.validatePasswordResetToken(token)){
                passwordResetService.resetPassword(token, newPassword);
                return ResponseEntity.ok("Password has been reset successfully");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("expired token");
        } catch (InvalidResetPasswordTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request");
        }
    }
}
