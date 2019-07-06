package io.github.therealmone.tdf4j.module.lexer;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonLexerModuleTest {

    @Test
    public void normal() throws Exception {
        final AbstractLexerModule module = new JsonLexerModule("{\n" +
                "  \"terminals\" : [\n" +
                "    {\"tag\": \"tag1\", \"pattern\": \"pattern1\", \"priority\": 1},\n" +
                "    {\"tag\": \"tag2\", \"pattern\": \"pattern2\"},\n" +
                "    {\"tag\": \"tag3\", \"pattern\": \"pattern3\", \"priority\": 10000}\n" +
                "  ]\n" +
                "}").build();
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
    public void no_terminals() throws Exception {
        final AbstractLexerModule module = new JsonLexerModule("{\n" +
                "  \"terminals\" : [\n" +
                "  ]\n" +
                "}").build();
        assertEquals(0, module.getTerminals().size());
    }

    @Test(expected = ParseException.class)
    public void parse_fail() throws Exception {
        new JsonLexerModule("").build();
    }
}
