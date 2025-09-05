package api.utils;

import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.exception.WebClientException;
import com.gearit.api.utils.validator.UserProviderValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserProviderValidatorTest {

    @Test
    void passIfEmailAndPasswordValid() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail("valid@mail.ru");
        userProvider.setPassword("ValidPassword!1");
        userProvider.setProvider(TypeProvider.INTERNAL);

        assertDoesNotThrow(() -> UserProviderValidator.validateUserProvider(userProvider));
    }

    @Test
    void failIfEmailIncorrect() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail("not_valid.ru");
        userProvider.setPassword("ValidPassword!1");
        userProvider.setProvider(TypeProvider.INTERNAL);

        WebClientException ex = assertThrows(
                WebClientException.class,
                () -> UserProviderValidator.validateUserProvider(userProvider)
        );

        assertTrue(ex.getMessage().contains("Incorrect user data"));
        assertEquals("Invalid email format", ex.getExtendedHelp());
    }

    @Test
    void failIfEmailIsNull() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail(null);
        userProvider.setPassword("ValidPassword!1");
        userProvider.setProvider(TypeProvider.INTERNAL);

        WebClientException ex = assertThrows(
                WebClientException.class,
                () -> UserProviderValidator.validateUserProvider(userProvider)
        );

        assertTrue(ex.getMessage().contains("Incorrect user data"));
        assertEquals("Email cannot be null or empty", ex.getExtendedHelp());
    }

    @Test
    void failIfPasswordIsNull() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail("valid@mail.ru");
        userProvider.setPassword(null);
        userProvider.setProvider(TypeProvider.INTERNAL);

        WebClientException ex = assertThrows(
                WebClientException.class,
                () -> UserProviderValidator.validateUserProvider(userProvider)
        );

        assertTrue(ex.getMessage().contains("Incorrect user data"));
        assertEquals("Password cannot be null or empty", ex.getExtendedHelp());
    }

    @Test
    void failIfTypeProviderNonInternal() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail("valid@mail.ru");
        userProvider.setPassword(null);
        userProvider.setProvider(TypeProvider.OAUTH);

        assertDoesNotThrow(() -> UserProviderValidator.validateUserProvider(userProvider));
    }

    @Test
    void failIfPasswordIncorrect() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail("valid@mail.ru");
        userProvider.setPassword("not_valid_password");
        userProvider.setProvider(TypeProvider.INTERNAL);

        WebClientException ex = assertThrows(
                WebClientException.class,
                () -> UserProviderValidator.validateUserProvider(userProvider)
        );

        assertTrue(ex.getMessage().contains("Incorrect user data"));
        assertEquals(
                "Password must contain at least one lowercase letter, " +
                        "one uppercase letter, one digit, one special character," +
                        " and must not be empty.", ex.getExtendedHelp());
    }

    @Test
    void failIfPasswordLengthIncorrect() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail("valid@mail.ru");
        userProvider.setPassword("qwerty");
        userProvider.setProvider(TypeProvider.INTERNAL);

        WebClientException ex = assertThrows(
                WebClientException.class,
                () -> UserProviderValidator.validateUserProvider(userProvider)
        );

        assertTrue(ex.getMessage().contains("Incorrect user data"));
        assertEquals("Password length should be at least 10 characters", ex.getExtendedHelp());
    }
}
