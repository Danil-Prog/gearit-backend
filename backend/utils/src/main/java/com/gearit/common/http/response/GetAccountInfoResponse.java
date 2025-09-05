package com.gearit.common.http.response;

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
