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

package io.github.tdf4j.generator;

import io.github.tdf4j.core.model.Token;
import io.github.tdf4j.lexer.Lexer;
import io.github.tdf4j.lexer.SymbolListener;
import io.github.tdf4j.core.module.LexerAbstractModule;
import io.github.tdf4j.core.module.LexerJsonModule;
import io.github.tdf4j.core.module.LexerXmlModule;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class LexerFactoryTest {
    private final StringBuilder text = new StringBuilder();
    private final SymbolListener listener = new SymbolListener() {
        @Override
        public void listen(char ch) {
            text.append(ch);
        }

        @Override
        public int line() {
            return 0;
        }

        @Override
        public int column() {
            return 0;
        }

        @Override
        public void reset() {
        }
    };

    @Before
    public void before() {
        text.replace(0, text.length(), "");
    }

    @Test
    public void from_json_string() throws Exception {
        final Lexer lexer = Lexer.get(new LexerJsonModule("{\n" +
                "  \"terminals\": [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "    {\"tag\": \"ws\", \"pattern\": \"\\s|\\n|\\r\", \"priority\": 20000, \"hidden\": true}\n" +
                "  ]\n" +
                "}"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
    }

    @Test
    public void from_json_string_with_listener() throws Exception {
        final Lexer lexer = Lexer.get(new LexerJsonModule("{\n" +
                "  \"terminals\": [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "    {\"tag\": \"ws\", \"pattern\": \"\\s|\\n|\\r\", \"priority\": 20000, \"hidden\": true}\n" +
                "  ]\n" +
                "}"), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_json_input_stream() throws Exception {
        final Lexer lexer = Lexer.get(new LexerJsonModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.json")));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_json_input_stream_with_listener() throws Exception {
        final Lexer lexer = Lexer.get(new LexerJsonModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.json")), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @Test
    public void from_json_file() throws Exception {
        final Lexer lexer = Lexer.get(new LexerJsonModule(new File("src/test/resources/terminals.json")));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
    }

    @Test
    public void from_json_file_with_listener() throws Exception {
        final Lexer lexer = Lexer.get(new LexerJsonModule(new File("src/test/resources/terminals.json")), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @Test
    public void from_xml_string() throws Exception {
        final Lexer lexer = Lexer.get(new LexerXmlModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "  <terminal tag=\"ws\" pattern=\"\\s|\\n|\\r\" priority=\"20000\" hidden=\"true\"/>\n" +
                "</terminals>"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
    }

    @Test
    public void from_xml_string_with_listener() throws Exception {
        final Lexer lexer = Lexer.get(new LexerXmlModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "  <terminal tag=\"ws\" pattern=\"\\s|\\n|\\r\" priority=\"20000\" hidden=\"true\"/>\n" +
                "</terminals>"), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3").toList());
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_xml_input_stream() throws Exception {
        final Lexer lexer = Lexer.get(new LexerXmlModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.xml")));
        assertTokens(lexer.analyze("pattern1pattern2 pattern3").toList());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_xml_input_stream_with_listener() throws Exception {
        final Lexer lexer = Lexer.get(new LexerXmlModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.xml")), listener);
        assertTokens(lexer.analyze("pattern1pattern2 pattern3").toList());
        assertEquals("pattern1pattern2 pattern3", text.toString());
    }

    @Test
    public void from_xml_file() throws Exception {
        final Lexer lexer = Lexer.get(new LexerXmlModule(new File("src/test/resources/terminals.xml")));
        assertTokens(lexer.analyze("pattern1 pattern2pattern3").toList());
    }

    @Test
    public void from_xml_file_with_listener() throws Exception {
        final Lexer lexer = Lexer.get(new LexerXmlModule(new File("src/test/resources/terminals.xml")), listener);
        assertTokens(lexer.analyze("pattern1 pattern2pattern3").toList());
        assertEquals("pattern1 pattern2pattern3", text.toString());
    }

    @Test
    public void with_module() throws Exception {
        final Lexer lexer = Lexer.get(new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("tag1").pattern("pattern1").priority(1);
                tokenize("tag2").pattern("pattern2");
                tokenize("tag3").pattern("pattern3").priority(10000);
            }
        });
        assertTokens(lexer.analyze("pattern1pattern2pattern3").toList());
    }

    @Test
    public void with_module_and_listener() throws Exception {
        final Lexer lexer = Lexer.get(new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("tag1").pattern("pattern1").priority(1);
                tokenize("tag2").pattern("pattern2");
                tokenize("tag3").pattern("pattern3").priority(10000);
            }
        }, listener);
        assertTokens(lexer.analyze("pattern1pattern2pattern3").toList());
        assertEquals("pattern1pattern2pattern3", text.toString());
    }

    private void assertTokens(final List<Token> tokens) {
        assertEquals(3, tokens.size());
        {
            assertEquals("TAG1", tokens.get(0).getTag().getValue());
            assertEquals("pattern1", tokens.get(0).getValue());
        }
        {
            assertEquals("TAG2", tokens.get(1).getTag().getValue());
            assertEquals("pattern2", tokens.get(1).getValue());
        }
        {
            assertEquals("TAG3", tokens.get(2).getTag().getValue());
            assertEquals("pattern3", tokens.get(2).getValue());
        }
    }

}
