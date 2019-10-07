/*
 * Copyright (c) 2019 tdf4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.tdf4j.core.utils;

import io.github.tdf4j.core.model.Stream;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

//Stream decorator
public class BufferedStream<T> implements Stream<T> {
    private final Stream<T> stream;
    private List<T> buffer;
    private int cursor;

    public BufferedStream(final Stream<T> stream) {
        this.buffer = new ArrayList<>();
        this.stream = stream;
    }

    @Override
    public Supplier<T> getGenerator() {
        return stream.getGenerator();
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
}
