package com.gearit.api.dto.view;

import com.gearit.api.entity.account.AccountInfo;
import java.time.Instant;

public record AccountInfoView(
        String firstName,
        String lastName,
        String avatarId,
        Instant birthDate
) {

    public static AccountInfoView from(AccountInfo accountInfo) {
        return new AccountInfoView(
                accountInfo.getFirstName(),
                accountInfo.getLastName(),
                accountInfo.getAvatarId(),
                accountInfo.getBirthDate()
        );
    }
}
