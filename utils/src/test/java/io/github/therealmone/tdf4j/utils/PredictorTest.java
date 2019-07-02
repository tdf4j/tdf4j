package io.github.therealmone.tdf4j.utils;

import io.github.therealmone.tdf4j.model.Token;
import io.github.therealmone.tdf4j.model.First;
import io.github.therealmone.tdf4j.model.ebnf.NonTerminal;
import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PredictorTest {

    @Test
    public void normal() {
        final First first = new First.Builder()
                .setSet(new HashMap<>() {{
                    put(nt("a"), tags("tag1", "tag2", "tag3"));
                    put(nt("b"), tags("tag2", "tag3"));
                    put(nt("c"), tags("tag3"));
                }}).build();
        final Predictor predictor = new Predictor(first, null);
        //tag1
        {
            final List<String> predictions = predictor.predict(token("tag1", "tag1"));
            assertEquals(2, predictions.size());
            assertEquals("a", predictions.get(0));
            assertEquals("tag1", predictions.get(1));
        }
        //tag2
        {
            final List<String> predictions = predictor.predict(token("tag2", "tag2"));
            assertEquals(3, predictions.size());
            assertEquals("a", predictions.get(0));
            assertEquals("b", predictions.get(1));
            assertEquals("tag2", predictions.get(2));
        }
        //tag3
        {
            final List<String> predictions = predictor.predict(token("tag3", "tag3"));
            assertEquals(4, predictions.size());
            assertEquals("a", predictions.get(0));
            assertEquals("b", predictions.get(1));
            assertEquals("c", predictions.get(2));
            assertEquals("tag3", predictions.get(3));
        }
        //check cache
        {
            predictor.predict(token("tag1", "tag1"));
            predictor.predict(token("tag2", "taп2"));
            predictor.predict(token("tag3", "tag3"));
            final Map<Terminal.Tag, List<String>> cache = predictor.cache;
            final Terminal.Tag tag1 = new Terminal.Tag.Builder().setValue("tag1").build();
            final Terminal.Tag tag2 = new Terminal.Tag.Builder().setValue("tag2").build();
            final Terminal.Tag tag3 = new Terminal.Tag.Builder().setValue("tag3").build();
            assertEquals(3, cache.size());
            assertTrue(cache.containsKey(tag1));
            assertTrue(cache.containsKey(tag2));
            assertTrue(cache.containsKey(tag3));
            assertEquals(2, cache.get(tag1).size());
            assertEquals(3, cache.get(tag2).size());
            assertEquals(4, cache.get(tag3).size());
        }
    }

    @Test
    public void empty_first_set() {
        final First first = new First.Builder().setSet(new HashMap<>()).build();
        final Predictor predictor = new Predictor(first, null);
        assertEquals(1, predictor.predict(token("tag1", "tag1")).size());
    }

    private NonTerminal nt(final String ident) {
        return new NonTerminal.Builder().setIdentifier(ident).build();
    }

    private Set<Terminal.Tag> tags(final String ... tags) {
        return new HashSet<>() {{
            for (final String tag : tags) {
                add(new Terminal.Tag.Builder().setValue(tag).build());
            }
        }};
    }

    private Token token(final String tag, final String value) {
        return new Token.Builder().setTag(new Terminal.Tag.Builder().setValue(tag).build()).setValue(value).build();
    }
}
