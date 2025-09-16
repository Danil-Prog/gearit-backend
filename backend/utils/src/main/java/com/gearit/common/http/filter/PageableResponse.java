package com.gearit.common.http.filter;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<PageableResponse<?>> toResponseEntity(Page<?> page, PageableRequest<?> pageable) {
        return ResponseEntity.ok(of(page, pageable));
    }

    private static PageableResponse<?> of(Page<?> page, PageableRequest<?> pageable) {
        return new PageableResponse<>(page.getTotalElements(), page.getContent(), pageable);
    }
}
