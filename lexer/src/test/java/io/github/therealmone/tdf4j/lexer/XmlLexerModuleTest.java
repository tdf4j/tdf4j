package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import io.github.therealmone.tdf4j.lexer.config.XmlLexerModule;
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
            assertEquals("tag1", module.getTerminals().get(0).tag());
            assertEquals("pattern1", module.getTerminals().get(0).pattern().pattern());
            assertEquals(1, module.getTerminals().get(0).priority());
        }
        {
            assertEquals("tag2", module.getTerminals().get(1).tag());
            assertEquals("pattern2", module.getTerminals().get(1).pattern().pattern());
            assertEquals(0, module.getTerminals().get(1).priority());
        }
        {
            assertEquals("tag3", module.getTerminals().get(2).tag());
            assertEquals("pattern3", module.getTerminals().get(2).pattern().pattern());
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
