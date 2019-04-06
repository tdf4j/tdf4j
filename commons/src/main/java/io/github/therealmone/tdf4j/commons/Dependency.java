package io.github.therealmone.tdf4j.commons;

import org.immutables.value.Value;

@Value.Immutable
public interface Dependency<T> {

    Class<? extends T> clazz();

    default Class<? extends T> getClazz() {
        return clazz();
    }

    String name();

    default String getName() {
        return name();
    }

    @Value.Default
    default T instance() {
        try {
            return clazz().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class Builder<T> extends ImmutableDependency.Builder<T> {
    }
}
