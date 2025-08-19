package com.gearit.api.dto.view;

import com.gearit.api.entity.accesspolicy.AccessPolicy;
import com.gearit.api.entity.user.TypeProvider;
import com.gearit.api.entity.user.UserProvider;

public record UserProviderView(
        Long id,
        String email,
        TypeProvider provider,
        AccessPolicy accessPolicy,
        AccountInfoView accountInfoView
) {

    public static UserProviderView from(UserProvider userProvider) {
        return new UserProviderView(
                userProvider.getId(),
                userProvider.getEmail(),
                userProvider.getProvider(),
                userProvider.getAccessPolicy(),
                AccountInfoView.from(userProvider.getAccountInfo())
        );
    }
}
