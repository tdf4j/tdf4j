package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.model.Token;
import io.github.therealmone.tdf4j.lexer.impl.LexerImpl;
import io.github.therealmone.tdf4j.lexer.impl.SymbolListenerImpl;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HiddenTerminalsTest {

    @Test
    public void white_spaces() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("ws").pattern("\\s|\\n|\\r").hidden(true);
            }
        }.build(), new SymbolListenerImpl());
        assertEquals(0, lexer.analyze("   \n  \r \n\r  \r\n").size());
    }

    @Test
    public void single_line_comment() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("single_line_comment").pattern("//.*(\n|\r|\r\n|\n\r)").hidden(true);
                tokenize("VAR").pattern("[A-Z]+");
            }
        }.build(), new SymbolListenerImpl());
        final List<Token> tokens = lexer.analyze("//comment\nA//comment\rB//comment\r\nC");
        assertEquals(3, tokens.size());
        assertEquals("A", tokens.get(0).value());
        assertEquals("B", tokens.get(1).value());
        assertEquals("C", tokens.get(2).value());
    }

    @Test
    public void multi_line_comment() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("multi_line_comment").pattern("/\\*[^(\\*/)]*\\*/").hidden(true);
                tokenize("VAR").pattern("[A-Z]+");
            }
        }.build(), new SymbolListenerImpl());
        final List<Token> tokens = lexer.analyze("/*comment\n\r\n\n*/A/*comment*/B/*\ncomment\n*/C");
        assertEquals(3, tokens.size());
        assertEquals("A", tokens.get(0).value());
        assertEquals("B", tokens.get(1).value());
        assertEquals("C", tokens.get(2).value());
    }
}
