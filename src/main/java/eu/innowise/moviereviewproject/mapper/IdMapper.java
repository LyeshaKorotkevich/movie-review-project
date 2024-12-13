package eu.innowise.moviereviewproject.mapper;

import java.util.HashMap;
import java.util.Map;

public class IdMapper<T> {
    private final Map<T, Long> toLongMap = new HashMap<>();
    private final Map<Long, T> toOriginalMap = new HashMap<>();
    private long currentId = 1L;

    public long getOrAddId(T original) {
        return toLongMap.computeIfAbsent(original, key -> {
            long id = currentId++;
            toOriginalMap.put(id, original);
            return id;
        });
    }

    public T getOriginal(long id) {
        return toOriginalMap.get(id);
    }
}
