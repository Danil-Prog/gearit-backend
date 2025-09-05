package com.gearit.api.entity.account;

import lombok.Getter;

@Getter
public enum AccountGender {

    MALE("male"),
    FEMALE("female"),
    ;

    final String name;

    AccountGender(String name) {
        this.name = name;
    }
}
