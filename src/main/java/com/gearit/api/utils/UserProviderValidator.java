package com.gearit.api.utils;

import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.BadRequestException;

public class UserProviderValidator {

    public static void validateUserProvider(UserProvider userProvider) {
        validateUsername(userProvider.getUsername());
        validateEmail(userProvider.getEmail());
        validatePassword(userProvider.getPassword());
    }

    private static void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Username cannot be null or empty");
        }

        if (!username.matches("^[a-zA-Z0-9_-]{3,16}$")) {
            throw new BadRequestException("Username contains invalid characters");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new BadRequestException("Email cannot be null or empty");
        }

        if (!email.matches("^[a-zA-Z0-9_-]{3,16}$")) {
            throw new BadRequestException("Invalid email format");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new BadRequestException("Password cannot be null or empty");
        }

        if (!password.matches("^[a-zA-Z0-9_-]{6,16}$")) {
            throw new BadRequestException("Invalid password format");
        }
    }
}
