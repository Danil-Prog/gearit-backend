package com.gearit.api.entity.account;

public enum AccountGender {

    MALE("male"),
    FEMALE("female"),
    ;

    final String name;

    AccountGender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
