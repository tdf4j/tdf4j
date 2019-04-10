package io.github.therealmone.tdf4j.commons.utils;

import io.github.therealmone.tdf4j.commons.Anchor;
import io.github.therealmone.tdf4j.commons.Revertable;
import io.github.therealmone.tdf4j.commons.Stream;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

//Stream decorator
public class BufferedStream<T> implements Stream<T>, Revertable<BufferedStream<T>> {
    private final Stream<T> stream;
    private List<T> buffer;
    private int cursor;

    public BufferedStream(final Stream<T> stream) {
        this.buffer = new ArrayList<>();
        this.stream = stream;
    }

    @Override
    public Supplier<T> generator() {
        return stream.generator();
    }

    @Nullable
    @Override
    public T next() {
        fillBuffer();
        return buffer.get(cursor++);
    }

    @Nullable
    public T peek() {
        fillBuffer();
        return buffer.get(cursor);
    }

    private void fillBuffer() {
        while(buffer.size() <= cursor) {
            buffer.add(stream.next());
        }
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        stream.forEach(action);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BufferedStream<T> revert(Anchor anchor) {
        try {
            this.cursor = ((AnchorImpl) anchor).getCursor();
            return this;
        } catch (ClassCastException e) {
            throw new RuntimeException("Invalid anchor", e);
        }
    }

    @Override
    public Anchor setAnchor() {
        return new AnchorImpl(cursor);
    }

    private class AnchorImpl implements Anchor {
        private final int cursor;

        AnchorImpl(final int cursor) {
            this.cursor = cursor;
        }

        int getCursor() {
            return cursor;
        }
    }
}
