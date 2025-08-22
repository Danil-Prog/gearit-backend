package com.gearit.api.utils.http;

import com.gearit.api.exception.WebClientException;
import com.gearit.api.utils.http.filter.FilterContainer;
import com.gearit.api.utils.http.filter.Filter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class PageableRequest<T> {

    private Integer page;
    private Integer size;
    private FilterContainer container;

    public PageableRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getOffset() {
        return (page - 1) * size;
    }

    public PageRequest toPageable() {
        return PageRequest.of(page, size);
    }

    public Specification<T> toSpecification() {
        if (container == null) {
            return null;
        }

        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate[] predicates = container.getFilters()
                    .stream()
                    .map((filter) -> toPredicate(root, criteriaBuilder, filter))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }

    /**
     * Формирует Predicate по переданному условию.
     */
    private Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Filter filter) {
        Predicate predicate;
        try {
            predicate = switch (filter.getCondition()) {
                case EQUALS -> criteriaBuilder.equal(root.get(filter.getField()), filter.getValue());

                case NOT_EQUALS -> criteriaBuilder.notEqual(root.get(filter.getField()), filter.getValue());

                case GREATER_THAN ->
                        criteriaBuilder.greaterThan(root.get(filter.getField()), (Integer) filter.getValue());

                case LESS_THAN -> criteriaBuilder.lessThan(root.get(filter.getField()), (Integer) filter.getValue());

                case CONTAINS -> criteriaBuilder.like(root.get(filter.getField()), "%" + filter.getValue() + "%");
            };
        } catch (Exception e) {
            throw asWebClientException(
                    String.format("Filter mapping error. Field: [%s], value: [%s]", filter.getField(), filter.getValue()),
                    e.getMessage()
            );
        }

        return predicate;
    }

    private WebClientException asWebClientException(String message, String extendedHelp) {
        return new WebClientException(
                message,
                extendedHelp
        );
    }
}
