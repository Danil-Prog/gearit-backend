package com.gearit.common.http.pageable.sort;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import static com.gearit.common.utils.EnumConverter.toEnum;

@Getter
@Setter
public class SortEntry {

    private String field;
    private SortDirection direction;

    public SortEntry(String field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public Sort toSort() {
        return Sort.by(toEnum(Sort.Direction.class, direction), field);
    }
}
