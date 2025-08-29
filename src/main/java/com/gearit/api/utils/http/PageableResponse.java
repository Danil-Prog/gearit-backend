package com.gearit.api.utils.http;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableResponse<T> {

    private Long total;
    private List<T> items;
    private int pages;
    private int page;

    private PageableResponse(long total, List<T> items, PageableRequest<?> pageable) {
        this.total = total;
        this.items = items;
        this.pages = (int) Math.ceil((double) this.total / pageable.getSize());
        this.page = pageable.getPage();
    }

    public static <T> PageableResponse<T> of(Long total, List<T> items, PageableRequest<?> pageable) {
        return new PageableResponse<>(total, items, pageable);
    }
}
