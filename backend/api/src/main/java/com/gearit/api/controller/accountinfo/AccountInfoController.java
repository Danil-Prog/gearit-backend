package com.gearit.api.controller.accountinfo;

import com.gearit.common.http.request.UpdateAccountInfoRequest;
import com.gearit.common.http.response.GetAccountInfoResponse;
import com.gearit.api.entity.account.AccountInfo;
import com.gearit.api.entity.user.UserProvider;
import com.gearit.api.service.profile.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountInfoController {

    private final AccountInfoService accountInfoService;

    @Autowired
    public AccountInfoController(AccountInfoService accountInfoService) {
        this.accountInfoService = accountInfoService;
    }

    @GetMapping
    public ResponseEntity<GetAccountInfoResponse> getAccountInfo(Authentication auth) {
        UserProvider userProvider = (UserProvider) auth.getPrincipal();
        AccountInfo accountInfo = userProvider.getAccountInfo();

        var response = new GetAccountInfoResponse(
                accountInfo.getFirstName(),
                accountInfo.getMiddleName(),
                accountInfo.getLastName(),
                userProvider.getEmail(),
                accountInfo.getPhoneNumber(),
                accountInfo.getGender(),
                accountInfo.getBirthDate(),
                accountInfo.getAvatarId()
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<Void> updateAccountInfo(
            @RequestBody UpdateAccountInfoRequest request,
            Authentication authentication
    ) {
        AccountInfo accountInfo = ((UserProvider) authentication.getPrincipal()).getAccountInfo();
        accountInfoService.updateAccountInfo(accountInfo, request);
        return ResponseEntity.ok().build();
    }
}
