package com.frankit.assignment.api.common.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final List<T> items; // 현재 페이지의 데이터 리스트
    private final long itemCount;

    private PageResponse(List<T> items, long itemCount) {
        this.items = items;
        this.itemCount = itemCount;
    }

    public static <T> PageResponse<T> of(List<T> items, long itemCount) {
        return new PageResponse<>(items, itemCount);
    }

}
