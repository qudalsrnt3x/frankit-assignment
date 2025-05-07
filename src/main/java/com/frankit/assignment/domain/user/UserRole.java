package com.frankit.assignment.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ADMIN("관리자"),
    CUSTOMER("일반 고객")
    ;

    private final String text;

}
