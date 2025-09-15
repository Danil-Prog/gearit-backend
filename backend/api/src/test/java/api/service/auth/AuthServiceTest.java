package api.service.auth;

import com.gearit.common.http.response.TokenResponse;
import com.gearit.api.entity.actioncode.ActionCode;
import com.gearit.api.entity.actioncode.ActionType;
import com.gearit.api.entity.notification.NotificationTemplate;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.common.exception.WebClientException;
import com.gearit.api.service.actioncode.ActionCodeService;
import com.gearit.api.service.auth.AuthService;
import com.gearit.api.service.jwt.JwtTokenProvider;
import com.gearit.api.service.notification.NotificationService;
import com.gearit.api.service.user.UserProviderService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserProviderService userProviderService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ActionCodeService actionCodeService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AuthService authService;

    private final String email = "test@example.com";
    private final String password = "#password";

    @Test
    void passRegisterIfSuccess() {
        when(userProviderService.isUserProviderByEmailExist(email)).thenReturn(false);

        UserProvider userProvider = new UserProvider();
        userProvider.setId(1L);
        when(userProviderService.createUserProviderWithEmptyAccountInfo(any())).thenReturn(userProvider);

        ActionCode actionCode = new ActionCode();
        actionCode.setCode("action_code");
        when(actionCodeService.createCode(eq(1L), eq(ActionType.CONFIRM_USER))).thenReturn(actionCode);

        authService.register(email, password);

        verify(userProviderService).createUserProviderWithEmptyAccountInfo(any(UserProvider.class));
        verify(actionCodeService).createCode(eq(1L), eq(ActionType.CONFIRM_USER));
        verify(notificationService).createNotification(
                eq(NotificationTemplate.USER_CONFIRMED),
                any(UserProvider.class),
                eq(Map.of("code", "action_code"))
        );
    }

    @Test
    void failRegisterIfUserAlreadyExists() {
        when(userProviderService.isUserProviderByEmailExist(email)).thenReturn(true);

        WebClientException exception = assertThrows(
                WebClientException.class,
                () -> authService.register(email, password)
        );

        assertEquals("User with such data already exists", exception.getExtendedHelp());
    }

    @Test
    void passVerifyUserProviderSuccess() {
        ActionCode actionCode = new ActionCode();
        actionCode.setCode("action_code");
        actionCode.setUserProviderId(1L);

        UserProvider userProvider = new UserProvider();
        userProvider.setEmail(email);

        when(actionCodeService.findByCode("action_code")).thenReturn(actionCode);
        when(userProviderService.getUserProviderByIdOrThrow(1L)).thenReturn(userProvider);

        authService.verifyUserProvider("action_code");

        assertTrue(userProvider.getIsConfirmed());

        verify(userProviderService).updateUserProvider(userProvider);
        verify(actionCodeService).deleteByCode("action_code");
    }

    @Test
    void failVerifyUserProviderIfActionNotExists() {
        when(actionCodeService.findByCode("INVALID")).thenReturn(null);

        WebClientException exception = assertThrows(
                WebClientException.class,
                () -> authService.verifyUserProvider("INVALID")
        );

        assertEquals("Verification code sent is invalid.", exception.getExtendedHelp());
    }

    @Test
    void passLoginIfSuccess() {
        UserProvider userProvider = new UserProvider();
        userProvider.setEmail(email);
        userProvider.setIsConfirmed(true);

        when(userProviderService.getUserProviderByEmailOrThrow(email)).thenReturn(userProvider);
        when(jwtTokenProvider.generateAccessToken(email)).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(email)).thenReturn("refresh_token");

        TokenResponse tokenResponse = authService.login(email, password);

        assertEquals("access_token", tokenResponse.accessToken());
        assertEquals("refresh_token", tokenResponse.refreshToken());
    }

    @Test
    void failLoginIfUserNotConfirmed() {
        UserProvider userProvider = new UserProvider();
        userProvider.setIsConfirmed(false);

        when(userProviderService.getUserProviderByEmailOrThrow(email)).thenReturn(userProvider);

        WebClientException exception = assertThrows(WebClientException.class, () -> authService.login(email, password));

        assertEquals("User is not confirmed", exception.getExtendedHelp());
    }

    @Test
    void failLoginIfCredentialsInvalid() {
        UserProvider userProvider = new UserProvider();
        userProvider.setIsConfirmed(true);
        when(userProviderService.getUserProviderByEmailOrThrow(email)).thenReturn(userProvider);

        doThrow(new WebClientException("User authentication failed", "Invalid email or password"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        WebClientException exception = assertThrows(WebClientException.class, () -> authService.login(email, password));

        assertEquals("Invalid email or password", exception.getExtendedHelp());
    }
}
