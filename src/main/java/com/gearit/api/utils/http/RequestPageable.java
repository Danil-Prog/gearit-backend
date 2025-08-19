package com.gearit.api.utils.http;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

@Getter
@Setter
public class RequestPageable {

    private int page = NumberUtils.INTEGER_ONE;
    private int size = 10;

    public RequestPageable() {
    }

    public RequestPageable(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
