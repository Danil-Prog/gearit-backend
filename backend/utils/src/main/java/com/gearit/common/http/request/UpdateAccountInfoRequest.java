package com.gearit.common.http.request;

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
