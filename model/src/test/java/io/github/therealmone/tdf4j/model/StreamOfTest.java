package io.github.therealmone.tdf4j.model;

import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StreamOfTest {

    @Test
    public void normal() {
        final Stream<String> stream = Stream.of(new ArrayList<String>() {{
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
        final Thread thread1 = new Thread(() -> Stream.of(new ArrayList<String>() {{
            for (int i = 0; i < 1_000_000; i++) {
                add("String");
            }
        }}));

        final Thread thread2 = new Thread(() -> Stream.of(new ArrayList<String>() {{
            for (int i = 0; i < 1_000_000; i++) {
                add("String");
            }
        }}));

        thread1.run();
        thread2.run();
    }
}
