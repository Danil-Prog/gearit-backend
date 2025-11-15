package com.gearit.common.http.filter;

import com.gearit.common.exception.WebClientException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Getter
@Setter
public class PageableRequest<T> {

    private Integer page;
    private Integer size;
    private FilterContainer container;
    private SortEntry sort;

    public PageRequest getPageRequest() {
        return sort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, sort.toSort());
    }

    public Specification<T> getSpecification() {
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

    public void addFilter(Filter filter) {
        if (container != null) {
            container.getFilters().add(filter);
        } else {
            container = new FilterContainer(List.of(filter));
        }
    }

    /**
     * Формирует Predicate по переданному условию.
     */
    private Predicate toPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Filter filter) {
        Predicate predicate;
        try {
            Path path = getPathFromField(root, filter.getField());
            Object value = filter.getValue();

            predicate = switch (filter.getCondition()) {
                case EQUALS -> criteriaBuilder.equal(path, value);

                case NOT_EQUALS -> criteriaBuilder.notEqual(path, value);

                case GREATER_THAN -> criteriaBuilder.greaterThan(path, (Integer) value);

                case LESS_THAN -> criteriaBuilder.lessThan(path, (Integer) value);

                case CONTAINS -> criteriaBuilder.like(path, "%" + value + "%");
            };
        } catch (Exception e) {
            throw asWebClientException(
                    String.format(
                            "Filter mapping error. Field: [%s], value: [%s]",
                            filter.getField(),
                            filter.getValue()),
                    e.getMessage()
            );
        }

        return predicate;
    }

    private Path<?> getPathFromField(Root<T> root, String field) {
        String[] parts = field.split("\\.");
        Path<?> path = root;

        for (String part : parts) {
            path = path.get(part);
        }

        return path;
    }

    private WebClientException asWebClientException(String message, String extendedHelp) {
        return new WebClientException(message, extendedHelp);
    }
}
