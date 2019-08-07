/*
 * Copyright (c) 2019 Roman Fatnev
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

package org.tdf4j.module.lexer;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlLexerModuleTest {

    @Test
    public void normal() {
        final AbstractLexerModule module = new XmlLexerModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "</terminals>").build();
        assertEquals(3, module.getTerminals().size());
        {
            assertEquals("TAG1", module.getTerminals().get(0).getTag().getValue());
            assertEquals("pattern1", module.getTerminals().get(0).getPattern().pattern());
            assertEquals(1, module.getTerminals().get(0).priority());
        }
        {
            assertEquals("TAG2", module.getTerminals().get(1).getTag().getValue());
            assertEquals("pattern2", module.getTerminals().get(1).getPattern().pattern());
            assertEquals(0, module.getTerminals().get(1).priority());
        }
        {
            assertEquals("TAG3", module.getTerminals().get(2).getTag().getValue());
            assertEquals("pattern3", module.getTerminals().get(2).getPattern().pattern());
            assertEquals(10000, module.getTerminals().get(2).priority());
        }
    }

    @Test
    public void no_terminals() {
        final AbstractLexerModule module = new XmlLexerModule("<terminals></terminals>").build();
        assertEquals(0, module.getTerminals().size());
    }

    @Test(expected = RuntimeException.class)
    public void parse_exception() {
        new XmlLexerModule("").build();
    }
}
