package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.commons.bean.Token;
import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class LexerFactoryTest {
    private final LexerFactory factory = new LexerFactory();

    @Test
    public void from_json_string() throws Exception {
        final Lexer lexer = factory.fromJson("{\n" +
                "  \"terminals\": [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "  ]\n" +
                "}");
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_json_input_stream() throws Exception {
        final Lexer lexer = factory.fromJson(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.json"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_json_file() throws Exception {
        final Lexer lexer = factory.fromJson(new File("src/test/resources/terminals.json"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_xml_string() throws Exception {
        final Lexer lexer = factory.fromXml("<terminals>\n" +
                "  <terminal tag=\"tag1\" pattern=\"pattern1\" priority=\"1\"/>\n" +
                "  <terminal tag=\"tag2\" pattern=\"pattern2\"/>\n" +
                "  <terminal tag=\"tag3\" pattern=\"pattern3\" priority=\"10000\"/>\n" +
                "</terminals>");
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_xml_input_stream() throws Exception {
        final Lexer lexer = factory.fromXml(Thread.currentThread().getContextClassLoader().getResourceAsStream("terminals.xml"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void from_xml_file() throws Exception {
        final Lexer lexer = factory.fromXml(new File("src/test/resources/terminals.xml"));
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    @Test
    public void with_module() throws Exception {
        final Lexer lexer = factory.withModule(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("tag1").pattern("pattern1").priority(1);
                tokenize("tag2").pattern("pattern2");
                tokenize("tag3").pattern("pattern3").priority(10000);
            }
        });
        assertTokens(lexer.analyze("pattern1 pattern2 pattern3"));
    }

    private void assertTokens(final List<Token> tokens) {
        assertEquals(3, tokens.size());
        {
            assertEquals("tag1", tokens.get(0).tag());
            assertEquals("pattern1", tokens.get(0).value());
        }
        {
            assertEquals("tag2", tokens.get(1).tag());
            assertEquals("pattern2", tokens.get(1).value());
        }
        {
            assertEquals("tag3", tokens.get(2).tag());
            assertEquals("pattern3", tokens.get(2).value());
        }
    }
}
