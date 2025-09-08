package com.gearit.common.http.response;

import com.gearit.common.dto.AccountGenderDto;
import java.time.Instant;

public record GetAccountInfoResponse(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phoneNumber,
        AccountGenderDto gender,
        Instant birthDate,
        String avatarId
) {
}