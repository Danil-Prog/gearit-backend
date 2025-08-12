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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOldSocialLogin() {
        return oldSocialLogin;
    }

    public void setOldSocialLogin(String oldSocialLogin) {
        this.oldSocialLogin = oldSocialLogin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDefaultAvatarId() {
        return defaultAvatarId;
    }

    public void setDefaultAvatarId(String defaultAvatarId) {
        this.defaultAvatarId = defaultAvatarId;
    }

    public AccountInfo toAccountInfo() {
        LocalDate localDate = LocalDate.parse(birthday);
        Instant instant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

        AccountGender accountGender = Arrays.stream(AccountGender.values())
                .filter(gender -> gender.getName().equals(getSex()))
                .findFirst()
                .orElse(null);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFirstName(firstName);
        accountInfo.setLastName(lastName);
        accountInfo.setAvatarId(defaultAvatarId);
        accountInfo.setGender(accountGender);
        accountInfo.setBirthDate(instant);

        return accountInfo;
    }
}
