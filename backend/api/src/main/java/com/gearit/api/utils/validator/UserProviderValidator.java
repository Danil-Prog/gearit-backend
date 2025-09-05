package com.gearit.api.utils.validator;

import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.WebClientException;

/**
 * Валидация пользовательских данных при регистрации.
 * На проверку идут email и newPassword пользователя.
 */
public class UserProviderValidator {

    private static final String ERROR_MESSAGE = "Incorrect user data";

    public static void validateUserProvider(UserProvider userProvider) {
        isValidEmail(userProvider.getEmail());

        if (userProvider.getProvider() == TypeProvider.INTERNAL) {
            isValidPassword(userProvider.getPassword());
        }
    }

    private static void isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw asWebClientException("Email cannot be null or empty");
        }

        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw asWebClientException("Invalid email format");
        }
    }

    private static void isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw asWebClientException("Password cannot be null or empty");
        }

        if (password.length() < 10) {
            throw asWebClientException("Password length should be at least 10 characters");
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$")) {
            throw asWebClientException(
                    "Password must contain at least one lowercase letter, " +
                            "one uppercase letter, one digit, one special character," +
                            " and must not be empty."
            );
        }
    }

    private static WebClientException asWebClientException(String extendedHelp) {
        return new WebClientException(ERROR_MESSAGE, extendedHelp);
    }
}
