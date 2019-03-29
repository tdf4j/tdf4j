package io.github.therealmone.tdf4j.commons;

import io.github.therealmone.tdf4j.commons.utils.BufferedStream;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class BufferedStreamTest {
    private final List<Integer> testData = List.of(1, 2, 3, 4);

    @Test
    public void test() {
        final List<Integer> list = new ArrayList<>(testData);
        final BufferedStream<Integer> bufferedStream = new BufferedStream<>(new Stream.Builder<Integer>().generator(() -> {
            if(!list.isEmpty()) {
                final Integer value = list.get(0);
                list.remove(0);
                return value;
            } else {
                return null;
            }
        }).build());

        assertEquals(1, bufferedStream.peek().intValue());
        assertEquals(1, bufferedStream.peek().intValue());
        assertEquals(1, bufferedStream.next().intValue());
        assertEquals(2, bufferedStream.next().intValue());
        assertEquals(3, bufferedStream.peek().intValue());
        assertEquals(3, bufferedStream.next().intValue());
        assertEquals(4, bufferedStream.next().intValue());
        assertNull(bufferedStream.peek());
        assertNull(bufferedStream.next());
    }

    @Test
    public void revert() {
        final List<Integer> list = new ArrayList<>(testData);
        final BufferedStream<Integer> bufferedStream = new BufferedStream<>(new Stream.Builder<Integer>().generator(() -> {
            if(!list.isEmpty()) {
                final Integer value = list.get(0);
                list.remove(0);
                return value;
            } else {
                return null;
            }
        }).build());

        {
            final Anchor anchor = bufferedStream.setAnchor();
            assertEquals(1, bufferedStream.next().intValue());
            assertEquals(2, bufferedStream.next().intValue());
            assertEquals(3, bufferedStream.next().intValue());
            assertEquals(4, bufferedStream.next().intValue());
            bufferedStream.revert(anchor);
            assertEquals(1, bufferedStream.peek().intValue());
            assertEquals(1, bufferedStream.next().intValue());
            assertEquals(2, bufferedStream.peek().intValue());
            assertEquals(2, bufferedStream.next().intValue());
            assertEquals(3, bufferedStream.next().intValue());
            assertEquals(4, bufferedStream.next().intValue());
        }
    }
}
