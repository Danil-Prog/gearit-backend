package com.gearit.api.entity.actioncode;

import com.gearit.api.constants.TableNames;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Data;

@Data
@Entity
@Table(name = TableNames.ACTION_CODES)
public class ActionCode {

    @Id
    @Column(name = "user_provider_id")
    private Long userProviderId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;
}
