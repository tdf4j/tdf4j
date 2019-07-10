package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.model.Stream;
import io.github.therealmone.tdf4j.model.Token;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CharSequenceLexicalAnalyzeTest {
    private final Lexer lexer = new LexerGenerator(new LexerOptions.Builder().setModule(new AbstractLexerModule() {
        @Override
        protected void configure() {
            tokenize("A").pattern("A");
            tokenize("B").pattern("B");
            tokenize("C").pattern("C");
        }
    }).build()).generate();


    @Test
    public void from_string() {
        //analyze
        {
            final List<Token> tokens = lexer.analyze("ABC");
            assertEquals(3, tokens.size());
            assertEquals("A", tokens.get(0).getTag().getValue());
            assertEquals("A", tokens.get(0).getValue());
            assertEquals("B", tokens.get(1).getTag().getValue());
            assertEquals("B", tokens.get(1).getValue());
            assertEquals("C", tokens.get(2).getTag().getValue());
            assertEquals("C", tokens.get(2).getValue());
        }

        //stream
        {
            final Stream<Token> tokens = lexer.stream("ABC");
            assertEquals("A", tokens.next().getValue());
            assertEquals("B", tokens.next().getValue());
            assertEquals("C", tokens.next().getValue());
        }
    }

    @Test
    public void from_string_builder() {
        //analyze
        {
            final List<Token> tokens = lexer.analyze(new StringBuilder("ABC"));
            assertEquals(3, tokens.size());
            assertEquals("A", tokens.get(0).getTag().getValue());
            assertEquals("A", tokens.get(0).getValue());
            assertEquals("B", tokens.get(1).getTag().getValue());
            assertEquals("B", tokens.get(1).getValue());
            assertEquals("C", tokens.get(2).getTag().getValue());
            assertEquals("C", tokens.get(2).getValue());
        }

        //stream
        {
            final Stream<Token> tokens = lexer.stream(new StringBuilder("ABC"));
            assertEquals("A", tokens.next().getValue());
            assertEquals("B", tokens.next().getValue());
            assertEquals("C", tokens.next().getValue());
        }
    }

    @Test
    public void from_string_buffer() {
        //analyze
        {
            final List<Token> tokens = lexer.analyze(new StringBuffer("ABC"));
            assertEquals(3, tokens.size());
            assertEquals("A", tokens.get(0).getTag().getValue());
            assertEquals("A", tokens.get(0).getValue());
            assertEquals("B", tokens.get(1).getTag().getValue());
            assertEquals("B", tokens.get(1).getValue());
            assertEquals("C", tokens.get(2).getTag().getValue());
            assertEquals("C", tokens.get(2).getValue());
        }

        //stream
        {
            final Stream<Token> tokens = lexer.stream(new StringBuffer("ABC"));
            assertEquals("A", tokens.next().getValue());
            assertEquals("B", tokens.next().getValue());
            assertEquals("C", tokens.next().getValue());
        }
    }

}
