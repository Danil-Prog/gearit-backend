package com.gearit.api.entity.user;

import com.gearit.api.constants.*;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = TableNames.PASSWORD_RECOVERY)
public class PasswordRecovery {

    @Id
    @Column(name = "user_provider_id")
    private Long userProviderId;

    @Column(name = "recovery_code", nullable = false)
    private String code;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public Long getUserProviderId() {
        return userProviderId;
    }

    public void setUserProviderId(Long userProviderId) {
        this.userProviderId = userProviderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
