package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.model.Token;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.SymbolListener;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.lexer.JsonLexerModule;
import io.github.therealmone.tdf4j.module.lexer.XmlLexerModule;
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
        final Lexer lexer = new LexerGenerator(new JsonLexerModule("{\n" +
                "  \"terminals\": [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "    {\"tag\": \"ws\", \"pattern\": \"\\s|\\n|\\r\", \"priority\": 20000, \"hidden\": true}\n" +
                "  ]\n" +
                "}")).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_json_string_with_listener() throws Exception {
        final Lexer lexer = new LexerGenerator(new JsonLexerModule("{\n" +
                "  \"terminals\": [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "    {\"tag\": \"ws\", \"pattern\": \"\\s|\\n|\\r\", \"priority\": 20000, \"hidden\": true}\n" +
                "  ]\n" +
                "}"), listener).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_json_input_stream() throws Exception {
        final Lexer lexer = new LexerGenerator(new JsonLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.json"))).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_json_input_stream_with_listener() throws Exception {
        final Lexer lexer = new LexerGenerator(new JsonLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.json")), listener).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @Test
    public void from_json_file() throws Exception {
        final Lexer lexer = new LexerGenerator(new JsonLexerModule(new File("src/test/resources/terminals.json"))).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_json_file_with_listener() throws Exception {
        final Lexer lexer = new LexerGenerator(new JsonLexerModule(new File("src/test/resources/terminals.json")), listener).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @Test
    public void from_xml_string() throws Exception {
        final Lexer lexer = new LexerGenerator(new XmlLexerModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "  <terminal tag=\"ws\" pattern=\"\\s|\\n|\\r\" priority=\"20000\" hidden=\"true\"/>\n" +
                "</terminals>")).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_xml_string_with_listener() throws Exception {
        final Lexer lexer = new LexerGenerator(new XmlLexerModule("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "  <terminal tag=\"ws\" pattern=\"\\s|\\n|\\r\" priority=\"20000\" hidden=\"true\"/>\n" +
                "</terminals>"), listener).generate();
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
        assertEquals("pattern1 pattern2 pattern3", text.toString());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_xml_input_stream() throws Exception {
        final Lexer lexer = new LexerGenerator(new XmlLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.xml"))).generate();
        assertTokens(lexer.analyze("pattern1pattern2 pattern3"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void from_xml_input_stream_with_listener() throws Exception {
        final Lexer lexer = new LexerGenerator(new XmlLexerModule(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.xml")), listener).generate();
        assertTokens(lexer.analyze("pattern1pattern2 pattern3"));
        assertEquals("pattern1pattern2 pattern3", text.toString());
    }

    @Test
    public void from_xml_file() throws Exception {
        final Lexer lexer = new LexerGenerator(new XmlLexerModule(new File("src/test/resources/terminals.xml"))).generate();
        assertTokens(lexer.analyze("pattern1 pattern2pattern3"));
    }

    @Test
    public void from_xml_file_with_listener() throws Exception {
        final Lexer lexer = new LexerGenerator(new XmlLexerModule(new File("src/test/resources/terminals.xml")), listener).generate();
        assertTokens(lexer.analyze("pattern1 pattern2pattern3"));
        assertEquals("pattern1 pattern2pattern3", text.toString());
    }

    @Test
    public void with_module() throws Exception {
        final Lexer lexer = new LexerGenerator(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("tag1").pattern("pattern1").priority(1);
                tokenize("tag2").pattern("pattern2");
                tokenize("tag3").pattern("pattern3").priority(10000);
            }
        }).generate();
        assertTokens(lexer.analyze("pattern1pattern2pattern3"));
    }

    @Test
    public void with_module_and_listener() throws Exception {
        final Lexer lexer = new LexerGenerator(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("tag1").pattern("pattern1").priority(1);
                tokenize("tag2").pattern("pattern2");
                tokenize("tag3").pattern("pattern3").priority(10000);
            }
        }, listener).generate();
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
}
