package io.github.therealmone.tdf4j.commons;

import org.immutables.value.Value;

@Value.Immutable
public interface Dependency<T> {

    Class<? extends T> clazz();

    String name();

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
