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

import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StreamOfTest {

    @Test
    public void normal() {
        final Stream<String> stream = Stream.of(new ArrayList<>() {{
            add("A");
            add("B");
            add("C");
        }});
        assertEquals("A", stream.next());
        assertEquals("B", stream.next());
        assertEquals("C", stream.next());
        assertNull(stream.next());
    }

    @Test
    public void emptyList() {
        final Stream<String> stream = Stream.of(new ArrayList<>());
        assertNull(stream.next());
    }

    @Test
    public void multi_thread() {
        final Thread thread1 = new Thread(() -> Stream.of(new ArrayList<>() {{
            for (int i = 0; i < 1_000_000; i++) {
                add("String");
            }
        }}));

        final Thread thread2 = new Thread(() -> Stream.of(new ArrayList<>() {{
            for (int i = 0; i < 1_000_000; i++) {
                add("String");
            }
        }}));

        thread1.run();
        thread2.run();
    }
}
