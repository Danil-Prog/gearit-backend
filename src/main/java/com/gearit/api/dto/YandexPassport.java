package com.gearit.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gearit.api.entity.account.AccountGender;
import com.gearit.api.entity.account.AccountInfo;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexPassport {

    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("display_name")
    private String displayName;

    private List<String> emails;

    @JsonProperty("default_email")
    private String defaultEmail;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("login")
    private String login;

    @JsonProperty("old_social_login")
    private String oldSocialLogin;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("default_avatar_id")
    private String defaultAvatarId;

    public AccountInfo toAccountInfo() {
        AccountInfo accountInfo = new AccountInfo();

        AccountGender accountGender = Arrays.stream(AccountGender.values())
                .filter(gender -> gender.getName().equals(getSex()))
                .findFirst()
                .orElse(null);

        accountInfo.setFirstName(firstName);
        accountInfo.setLastName(lastName);
        accountInfo.setAvatarId(defaultAvatarId);
        accountInfo.setGender(accountGender);

        if (birthday != null && !birthday.isEmpty()) {
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                Instant instant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                accountInfo.setBirthDate(instant);
            } catch (Exception exception) {
                exception.printStackTrace(System.err);
            }
        }

        return accountInfo;
    }
}
