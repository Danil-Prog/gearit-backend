package com.gearit.common.http.request;

import com.gearit.common.dto.AccountGenderDto;
import java.time.Instant;

public record UpdateAccountInfoRequest(
        String firstName,
        String middleName,
        String lastName,
        String phoneNumber,
        AccountGenderDto gender,
        Instant birthDate
) {
}
