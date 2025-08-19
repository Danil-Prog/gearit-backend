package com.gearit.api.utils.http;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
public class PageableRequest {

    private int page = NumberUtils.INTEGER_ONE;
    private int size = 10;

    public PageableRequest() {
    }

    public PageableRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public PageRequest toPageable() {
        return PageRequest.of(page, size);
    }
}
