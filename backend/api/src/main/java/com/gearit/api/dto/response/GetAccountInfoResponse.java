package com.gearit.api.dto.response;

import com.gearit.api.entity.account.AccountGender;
import java.time.Instant;

public record GetAccountInfoResponse(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phoneNumber,
        AccountGender gender,
        Instant birthDate,
        String avatarId
) {
}
