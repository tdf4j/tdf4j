package io.github.therealmone.tdf4j.commons;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

//Stream decorator
public class BufferedStream<T> implements Stream<T> {
    private final Stream<T> stream;
    private T buffer;

    public BufferedStream(final Stream<T> stream) {
        this.stream = stream;
    }

    @Override
    public Callable<T> generator() {
        return stream.generator();
    }

    @Nullable
    @Override
    public T next() {
        peek();
        final T value = buffer;
        buffer = null;
        return value;
    }

    @Nullable
    public T peek() {
        if(buffer == null) {
            buffer = stream.next();
        }
        return buffer;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        stream.forEach(action);
    }
}
