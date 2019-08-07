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

package org.tdf4j.generator;

import org.tdf4j.generator.impl.LexerGenerator;
import org.tdf4j.model.Token;
import org.tdf4j.lexer.Lexer;
import org.tdf4j.lexer.SymbolListener;
import org.tdf4j.module.lexer.AbstractLexerModule;
import org.tdf4j.module.lexer.JsonLexerModule;
import org.tdf4j.module.lexer.XmlLexerModule;
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
        final Lexer lexer = generate(new JsonLexerModule("{\n" +
                "  \"terminals\": [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "    {\"tag\": \"ws\", \"pattern\": \"\\s|\\n|\\r\", \"priority\": 20000, \"hidden\": true}\n" +
                "  ]\n" +
                "}"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_json_string_with_listener() throws Exception {
        final Lexer lexer = generate(new JsonLexerModule("{\n" +
                "  \"terminals\": [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "    {\"tag\": \"ws\", \"pattern\": \"\\s|\\n|\\r\", \"priority\": 20000, \"hidden\": true}\n" +
                "  ]\n" +
                "}"), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_json_input_stream() throws Exception {
        final Lexer lexer = generate(new JsonLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.json")));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_json_input_stream_with_listener() throws Exception {
        final Lexer lexer = generate(new JsonLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.json")), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @Test
    public void from_json_file() throws Exception {
        final Lexer lexer = generate(new JsonLexerModule(new File("src/test/resources/terminals.json")));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_json_file_with_listener() throws Exception {
        final Lexer lexer = generate(new JsonLexerModule(new File("src/test/resources/terminals.json")), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @Test
    public void from_xml_string() throws Exception {
        final Lexer lexer = generate(new XmlLexerModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "  <terminal tag=\"ws\" pattern=\"\\s|\\n|\\r\" priority=\"20000\" hidden=\"true\"/>\n" +
                "</terminals>"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_xml_string_with_listener() throws Exception {
        final Lexer lexer = generate(new XmlLexerModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "  <terminal tag=\"ws\" pattern=\"\\s|\\n|\\r\" priority=\"20000\" hidden=\"true\"/>\n" +
                "</terminals>"), listener);
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_xml_input_stream() throws Exception {
        final Lexer lexer = generate(new XmlLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.xml")));
        assertTokens(lexer.analyze("pattern1pattern2 pattern3"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_xml_input_stream_with_listener() throws Exception {
        final Lexer lexer = generate(new XmlLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.xml")), listener);
        assertTokens(lexer.analyze("pattern1pattern2 pattern3"));
        assertEquals("pattern1pattern2 pattern3", text.toString());
    }

    @Test
    public void from_xml_file() throws Exception {
        final Lexer lexer = generate(new XmlLexerModule(new File("src/test/resources/terminals.xml")));
        assertTokens(lexer.analyze("pattern1 pattern2pattern3"));
    }

    @Test
    public void from_xml_file_with_listener() throws Exception {
        final Lexer lexer = generate(new XmlLexerModule(new File("src/test/resources/terminals.xml")), listener);
        assertTokens(lexer.analyze("pattern1 pattern2pattern3"));
        assertEquals("pattern1 pattern2pattern3", text.toString());
    }

    @Test
    public void with_module() throws Exception {
        final Lexer lexer = generate(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("tag1").pattern("pattern1").priority(1);
                tokenize("tag2").pattern("pattern2");
                tokenize("tag3").pattern("pattern3").priority(10000);
            }
        });
        assertTokens(lexer.analyze("pattern1pattern2pattern3"));
    }

    @Test
    public void with_module_and_listener() throws Exception {
        final Lexer lexer = generate(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("tag1").pattern("pattern1").priority(1);
                tokenize("tag2").pattern("pattern2");
                tokenize("tag3").pattern("pattern3").priority(10000);
            }
        }, listener);
        assertTokens(lexer.analyze("pattern1pattern2pattern3"));
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

    private Lexer generate(final AbstractLexerModule module, final SymbolListener listener) {
        return new LexerGenerator(new LexerOptions.Builder().setModule(module).setListener(listener).build()).generate();
    }

    private Lexer generate(final AbstractLexerModule module) {
        return new LexerGenerator(new LexerOptions.Builder().setModule(module).build()).generate();
    }
}
