package io.github.therealmone.tdf4j.utils;

import io.github.therealmone.tdf4j.model.ebnf.Follow;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FollowSetCollectorTest {
    private final FollowSetCollector followSetCollector = new FollowSetCollector();

    //todo
    @Test
    public void test() {
        final Follow follow = followSetCollector.collect(new ArrayList<>());
        assertNotNull(follow);
        assertEquals(0, follow.set().size());
    }

}
