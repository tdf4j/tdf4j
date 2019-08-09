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
package org.tdf4j.core.model;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Value.Immutable
public interface Stream<T> {

    Supplier<T> getGenerator();

    @Nullable
    default T next() {
        return getGenerator().get();
    }

    default void forEach(final Consumer<? super T> action) {
        T value;
        while((value = next()) != null) {
            action.accept(value);
        }
    }

    static <T> Stream<T> of(final List<T> list) {
        class Cursor {
            int value = 0;

            private void increase() {
                value++;
            }

            private int getValue() {
                return value;
            }
        }
        final Cursor cursor = new Cursor();
        return new Stream.Builder<T>()
                .setGenerator(() -> {
                    if(list.size() > cursor.getValue()) {
                        cursor.increase();
                        return list.get(cursor.getValue() - 1);
                    } else {
                        return null;
                    }
                }).build();
    }

    class Builder<T> extends ImmutableStream.Builder<T> {
    }
}
