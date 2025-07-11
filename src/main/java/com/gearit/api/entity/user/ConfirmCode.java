package com.gearit.api.entity.user;

import com.gearit.api.constants.TableNames;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = TableNames.CONFIRM_CODE)
public class ConfirmCode {

    @Id
    @Column(name = "user_provider_id")
    private Long userProviderId;

    @Column(name = "confirm_code")
    private String code;

    public Long getUserProviderId() {
        return userProviderId;
    }

    public void setUserProviderId(Long userProviderId) {
        this.userProviderId = userProviderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
