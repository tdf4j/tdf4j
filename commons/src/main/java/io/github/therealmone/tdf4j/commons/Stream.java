package io.github.therealmone.tdf4j.commons;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

@Value.Immutable
public interface Stream<T> {
    Callable<T> generator();

    @Nullable
    default T next() {
        try {
            return generator().call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default void forEach(final Consumer<? super T> action) {
        T value;
        while((value = next()) != null) {
            action.accept(value);
        }
    }

    class Builder<T> extends ImmutableStream.Builder<T> {
    }
}
