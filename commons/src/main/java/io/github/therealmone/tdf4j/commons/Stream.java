package io.github.therealmone.tdf4j.commons;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Value.Immutable
public interface Stream<T> {
    Supplier<T> generator();

    @Nullable
    default T next() {
        return generator().get();
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
