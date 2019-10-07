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

package io.github.tdf4j.core.module;

import org.junit.Test;
import io.github.tdf4j.core.model.Letter;

import java.util.List;

import static org.junit.Assert.*;

public class LexerXmlModuleTest {

    @Test
    public void normal() {
        final LexerAbstractModule module = new LexerXmlModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "</terminals>").build();
        assertNotNull(module.getAlphabet());
        final List<Letter> letters = module.getAlphabet().getLetters();
        assertEquals(3, letters.size());
        {
            final Letter letter_0 = letters.get(0);
            assertEquals("TAG1", letter_0.getTag().getValue());
            assertEquals("pattern1", letter_0.getPattern().pattern());
            assertEquals(1, letter_0.priority());
        }
        {
            final Letter letter_1 = letters.get(1);
            assertEquals("TAG2", letter_1.getTag().getValue());
            assertEquals("pattern2", letter_1.getPattern().pattern());
            assertEquals(0, letter_1.priority());
        }
        {
            final Letter letter_2 = letters.get(2);
            assertEquals("TAG3", letter_2.getTag().getValue());
            assertEquals("pattern3", letter_2.getPattern().pattern());
            assertEquals(10000, letter_2.priority());
        }
    }

    @Test
    public void no_terminals() {
        final LexerAbstractModule module = new LexerXmlModule("<terminals></terminals>").build();
        assertEquals(0, module.getAlphabet().getLetters().size());
    }

    @Test(expected = RuntimeException.class)
    public void parse_exception() {
        new LexerXmlModule("").build();
    }
}
