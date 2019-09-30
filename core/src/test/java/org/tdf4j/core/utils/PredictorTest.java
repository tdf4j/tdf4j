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

package org.tdf4j.core.utils;

import org.tdf4j.core.model.Token;
import org.tdf4j.core.model.First;
import org.tdf4j.core.model.ebnf.NonTerminal;
import org.tdf4j.core.model.ebnf.Terminal;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.tdf4j.core.model.ebnf.EBNFBuilder.*;

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
            assertEquals("TAG1", predictions.get(1));
        }
        //tag2
        {
            final List<String> predictions = predictor.predict(token("tag2", "tag2"));
            assertEquals(3, predictions.size());
            assertEquals("a", predictions.get(0));
            assertEquals("b", predictions.get(1));
            assertEquals("TAG2", predictions.get(2));
        }
        //tag3
        {
            final List<String> predictions = predictor.predict(token("tag3", "tag3"));
            assertEquals(4, predictions.size());
            assertEquals("a", predictions.get(0));
            assertEquals("b", predictions.get(1));
            assertEquals("c", predictions.get(2));
            assertEquals("TAG3", predictions.get(3));
        }
        //check cache
        {
            predictor.predict(token("tag1", "tag1"));
            predictor.predict(token("tag2", "taп2"));
            predictor.predict(token("tag3", "tag3"));
            final Map<Terminal, List<String>> cache = predictor.cache;
            final Terminal tag1 = terminal("tag1");
            final Terminal tag2 = terminal("tag2");
            final Terminal tag3 = terminal("tag3");
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
        return nonTerminal(ident);
    }

    private Set<Terminal> tags(final String ... tags) {
        return new HashSet<>() {{
            for (final String tag : tags) {
                add(terminal(tag));
            }
        }};
    }

    private Token token(final String tag, final String value) {
        return new Token.Builder().setTag(terminal(tag)).setValue(value).build();
    }
}
