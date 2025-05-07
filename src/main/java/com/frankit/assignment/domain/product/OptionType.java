package com.frankit.assignment.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptionType {
    INPUT("입력 타입"),
    SELECT("선택 타입")
    ;

    private final String text;

}
