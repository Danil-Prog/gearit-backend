package com.gearit.api.dto.request;

import com.gearit.api.entity.account.AccountGender;
import java.time.Instant;

public record UpdateAccountInfoRequest(
        String firstName,
        String middleName,
        String lastName,
        String phoneNumber,
        AccountGender gender,
        Instant birthDate
) {
}
