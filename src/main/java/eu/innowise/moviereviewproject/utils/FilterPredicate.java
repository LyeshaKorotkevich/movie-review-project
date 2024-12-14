package eu.innowise.moviereviewproject.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterPredicate {

    private final List<Predicate> predicates = new ArrayList<>();

    public static FilterPredicate builder() {
        return new FilterPredicate();
    }

    public <T> FilterPredicate add(T object, Function<T, Predicate> function) {
        if(Objects.nonNull(object)) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAnd(CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
