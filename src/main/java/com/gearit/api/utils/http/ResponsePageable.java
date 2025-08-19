package com.gearit.api.utils.http;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

@Getter
@Setter
public class ResponsePageable<T> {

    private Long records;
    private List<T> items;
    private int pages;
    private int page;

    private int recordFrom;
    private int recordTo;

    public ResponsePageable(int records, List<T> items, RequestPageable pageable) {
        this((long) records, items, pageable);
    }

    public ResponsePageable(long records, List<T> items, RequestPageable pageable) {
        this.records = records;
        this.items = items;
        this.pages = (int) Math.ceil((double) this.records / pageable.getSize());
        this.page = pageable.getPage();
        if (items.isEmpty()) {
            this.recordFrom = 1;
            this.recordTo = Math.toIntExact(this.records);
        } else {
            this.recordFrom = (this.page * pageable.getSize()) - pageable.getSize();
            this.recordTo = Math.toIntExact((this.page == pages) ? this.records : (long) this.page * pageable.getSize());
        }
    }
}
