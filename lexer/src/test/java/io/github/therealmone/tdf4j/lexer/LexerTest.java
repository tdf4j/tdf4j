package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.model.Stream;
import io.github.therealmone.tdf4j.model.Token;
import io.github.therealmone.tdf4j.lexer.impl.LexerImpl;
import io.github.therealmone.tdf4j.lexer.impl.SymbolListenerImpl;
import io.github.therealmone.tdf4j.lexer.utils.Config;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LexerTest {
    @Test
    //these are old tests from http://github.com/therealmone/SPOTranslator
    public void analyze() {
        Lexer lexer = new LexerImpl(new Config().build(), new SymbolListenerImpl());

        {
            final List<Token> tokens = lexer.analyze("value");
            assertEquals(1, tokens.size());
            assertEquals("VAR", tokens.get(0).tag().value());
            assertEquals("value", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("0");
            assertEquals(1, tokens.size());
            assertEquals("DIGIT", tokens.get(0).tag().value());
            assertEquals("0", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("=");
            assertEquals(1, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
            assertEquals("=", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("+");
            assertEquals(1, tokens.size());
            assertEquals("OP", tokens.get(0).tag().value());
        }

        {
            final List<Token> tokens = lexer.analyze("value = 15 + 0");
            assertEquals(5, tokens.size());
            assertEquals("VAR", tokens.get(0).tag().value());
            assertEquals("value", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("=", tokens.get(1).value());
            assertEquals("DIGIT", tokens.get(2).tag().value());
            assertEquals("15", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("+", tokens.get(3).value());
            assertEquals("DIGIT", tokens.get(4).tag().value());
            assertEquals("0", tokens.get(4).value());
        }

        {
            final List<Token> tokens = lexer.analyze("15 = value - 10");
            assertEquals(5, tokens.size());
            assertEquals("DIGIT", tokens.get(0).tag().value());
            assertEquals("15", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("=", tokens.get(1).value());
            assertEquals("VAR", tokens.get(2).tag().value());
            assertEquals("value", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("-", tokens.get(3).value());
            assertEquals("DIGIT", tokens.get(4).tag().value());
            assertEquals("10", tokens.get(4).value());
        }

        {
            final List<Token> tokens = lexer.analyze("= value / 0");
            assertEquals(4, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
            assertEquals("=", tokens.get(0).value());
            assertEquals("VAR", tokens.get(1).tag().value());
            assertEquals("value", tokens.get(1).value());
            assertEquals("OP", tokens.get(2).tag().value());
            assertEquals("/", tokens.get(2).value());
            assertEquals("DIGIT", tokens.get(3).tag().value());
            assertEquals("0", tokens.get(3).value());
        }

        {
            final List<Token> tokens = lexer.analyze("* = value - 100000");
            assertEquals(5, tokens.size());
            assertEquals("OP", tokens.get(0).tag().value());
            assertEquals("*", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("=", tokens.get(1).value());
            assertEquals("VAR", tokens.get(2).tag().value());
            assertEquals("value", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("-", tokens.get(3).value());
            assertEquals("DIGIT", tokens.get(4).tag().value());
            assertEquals("100000", tokens.get(4).value());
        }

        {
            final List<Token> tokens = lexer.analyze("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /");
            assertEquals(22, tokens.size());
            assertEquals("VAR", tokens.get(0).tag().value());
            assertEquals("value", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("=", tokens.get(1).value());
            assertEquals("VAR", tokens.get(2).tag().value());
            assertEquals("a", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("+", tokens.get(3).value());
            assertEquals("VAR", tokens.get(4).tag().value());
            assertEquals("b", tokens.get(4).value());
            assertEquals("OP", tokens.get(5).tag().value());
            assertEquals("-", tokens.get(5).value());
            assertEquals("VAR", tokens.get(6).tag().value());
            assertEquals("c", tokens.get(6).value());
            assertEquals("OP", tokens.get(7).tag().value());
            assertEquals("/", tokens.get(7).value());
            assertEquals("DIGIT", tokens.get(8).tag().value());
            assertEquals("0", tokens.get(8).value());
            assertEquals("OP", tokens.get(9).tag().value());
            assertEquals("*", tokens.get(9).value());
            assertEquals("DIGIT", tokens.get(10).tag().value());
            assertEquals("10045645", tokens.get(10).value());
            assertEquals("ASSIGN_OP", tokens.get(11).tag().value());
            assertEquals("=", tokens.get(11).value());
            assertEquals("VAR", tokens.get(12).tag().value());
            assertEquals("value", tokens.get(12).value());
            assertEquals("DIGIT", tokens.get(13).tag().value());
            assertEquals("1", tokens.get(13).value());
            assertEquals("ASSIGN_OP", tokens.get(14).tag().value());
            assertEquals("=", tokens.get(14).value());
            assertEquals("VAR", tokens.get(15).tag().value());
            assertEquals("value", tokens.get(15).value());
            assertEquals("DIGIT", tokens.get(16).tag().value());
            assertEquals("2", tokens.get(16).value());
            assertEquals("ASSIGN_OP", tokens.get(17).tag().value());
            assertEquals("=", tokens.get(17).value());
            assertEquals("OP", tokens.get(18).tag().value());
            assertEquals("-", tokens.get(18).value());
            assertEquals("OP", tokens.get(19).tag().value());
            assertEquals("*", tokens.get(19).value());
            assertEquals("OP", tokens.get(20).tag().value());
            assertEquals("+", tokens.get(20).value());
            assertEquals("OP", tokens.get(21).tag().value());
            assertEquals("/", tokens.get(21).value());
        }

        {
            final List<Token> tokens = lexer.analyze("= - * + / value1 = value2 = a + b * 0 - 9999 = 10000 - 10000 * = value");
            assertEquals(25, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
            assertEquals("=", tokens.get(0).value());
            assertEquals("OP", tokens.get(1).tag().value());
            assertEquals("-", tokens.get(1).value());
            assertEquals("OP", tokens.get(2).tag().value());
            assertEquals("*", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag().value());
            assertEquals("+", tokens.get(3).value());
            assertEquals("OP", tokens.get(4).tag().value());
            assertEquals("/", tokens.get(4).value());
            assertEquals("VAR", tokens.get(5).tag().value());
            assertEquals("value", tokens.get(5).value());
            assertEquals("DIGIT", tokens.get(6).tag().value());
            assertEquals("1", tokens.get(6).value());
            assertEquals("ASSIGN_OP", tokens.get(7).tag().value());
            assertEquals("=", tokens.get(7).value());
            assertEquals("VAR", tokens.get(8).tag().value());
            assertEquals("value", tokens.get(8).value());
            assertEquals("DIGIT", tokens.get(9).tag().value());
            assertEquals("2", tokens.get(9).value());
            assertEquals("ASSIGN_OP", tokens.get(10).tag().value());
            assertEquals("=", tokens.get(10).value());
            assertEquals("VAR", tokens.get(11).tag().value());
            assertEquals("a", tokens.get(11).value());
            assertEquals("OP", tokens.get(12).tag().value());
            assertEquals("+", tokens.get(12).value());
            assertEquals("VAR", tokens.get(13).tag().value());
            assertEquals("b", tokens.get(13).value());
            assertEquals("OP", tokens.get(14).tag().value());
            assertEquals("*", tokens.get(14).value());
            assertEquals("DIGIT", tokens.get(15).tag().value());
            assertEquals("0", tokens.get(15).value());
            assertEquals("OP", tokens.get(16).tag().value());
            assertEquals("-", tokens.get(16).value());
            assertEquals("DIGIT", tokens.get(17).tag().value());
            assertEquals("9999", tokens.get(17).value());
            assertEquals("ASSIGN_OP", tokens.get(18).tag().value());
            assertEquals("=", tokens.get(18).value());
            assertEquals("DIGIT", tokens.get(19).tag().value());
            assertEquals("10000", tokens.get(19).value());
            assertEquals("OP", tokens.get(20).tag().value());
            assertEquals("-", tokens.get(20).value());
            assertEquals("DIGIT", tokens.get(21).tag().value());
            assertEquals("10000", tokens.get(21).value());
            assertEquals("OP", tokens.get(22).tag().value());
            assertEquals("*", tokens.get(22).value());
            assertEquals("ASSIGN_OP", tokens.get(23).tag().value());
            assertEquals("=", tokens.get(23).value());
            assertEquals("VAR", tokens.get(24).tag().value());
            assertEquals("value", tokens.get(24).value());
        }

        {
            final List<Token> tokens = lexer.analyze("= = = = = = = = = = = 10000 100 10 1 0 v a l u e * * * * - - - - / / /");
            assertEquals(32, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag().value());
            assertEquals("=", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag().value());
            assertEquals("=", tokens.get(1).value());
            assertEquals("ASSIGN_OP", tokens.get(2).tag().value());
            assertEquals("=", tokens.get(2).value());
            assertEquals("ASSIGN_OP", tokens.get(3).tag().value());
            assertEquals("=", tokens.get(3).value());
            assertEquals("ASSIGN_OP", tokens.get(4).tag().value());
            assertEquals("=", tokens.get(4).value());
            assertEquals("ASSIGN_OP", tokens.get(5).tag().value());
            assertEquals("=", tokens.get(5).value());
            assertEquals("ASSIGN_OP", tokens.get(6).tag().value());
            assertEquals("=", tokens.get(6).value());
            assertEquals("ASSIGN_OP", tokens.get(7).tag().value());
            assertEquals("=", tokens.get(7).value());
            assertEquals("ASSIGN_OP", tokens.get(8).tag().value());
            assertEquals("=", tokens.get(8).value());
            assertEquals("ASSIGN_OP", tokens.get(9).tag().value());
            assertEquals("=", tokens.get(9).value());
            assertEquals("ASSIGN_OP", tokens.get(10).tag().value());
            assertEquals("=", tokens.get(10).value());
            assertEquals("DIGIT", tokens.get(11).tag().value());
            assertEquals("10000", tokens.get(11).value());
            assertEquals("DIGIT", tokens.get(12).tag().value());
            assertEquals("100", tokens.get(12).value());
            assertEquals("DIGIT", tokens.get(13).tag().value());
            assertEquals("10", tokens.get(13).value());
            assertEquals("DIGIT", tokens.get(14).tag().value());
            assertEquals("1", tokens.get(14).value());
            assertEquals("DIGIT", tokens.get(15).tag().value());
            assertEquals("0", tokens.get(15).value());
            assertEquals("VAR", tokens.get(16).tag().value());
            assertEquals("v", tokens.get(16).value());
            assertEquals("VAR", tokens.get(17).tag().value());
            assertEquals("a", tokens.get(17).value());
            assertEquals("VAR", tokens.get(18).tag().value());
            assertEquals("l", tokens.get(18).value());
            assertEquals("VAR", tokens.get(19).tag().value());
            assertEquals("u", tokens.get(19).value());
            assertEquals("VAR", tokens.get(20).tag().value());
            assertEquals("e", tokens.get(20).value());
            assertEquals("OP", tokens.get(21).tag().value());
            assertEquals("*", tokens.get(21).value());
            assertEquals("OP", tokens.get(22).tag().value());
            assertEquals("*", tokens.get(22).value());
            assertEquals("OP", tokens.get(23).tag().value());
            assertEquals("*", tokens.get(23).value());
            assertEquals("OP", tokens.get(24).tag().value());
            assertEquals("*", tokens.get(24).value());
            assertEquals("OP", tokens.get(25).tag().value());
            assertEquals("-", tokens.get(25).value());
            assertEquals("OP", tokens.get(26).tag().value());
            assertEquals("-", tokens.get(26).value());
            assertEquals("OP", tokens.get(27).tag().value());
            assertEquals("-", tokens.get(27).value());
            assertEquals("OP", tokens.get(28).tag().value());
            assertEquals("-", tokens.get(28).value());
            assertEquals("OP", tokens.get(29).tag().value());
            assertEquals("/", tokens.get(29).value());
            assertEquals("OP", tokens.get(30).tag().value());
            assertEquals("/", tokens.get(30).value());
            assertEquals("OP", tokens.get(31).tag().value());
            assertEquals("/", tokens.get(31).value());
        }

        {
            final List<Token> tokens = lexer.analyze("!= !");
            assertEquals(2, tokens.size());
            assertEquals("COP", tokens.get(0).tag().value());
            assertEquals("!=", tokens.get(0).value());
            assertEquals("LOP", tokens.get(1).tag().value());
            assertEquals("!", tokens.get(1).value());
        }

        {
            final List<Token> tokens = lexer.analyze("0.155");
            assertEquals(1, tokens.size());
            assertEquals("DOUBLE", tokens.get(0).tag().value());
            assertEquals("0.155", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("155.0000000");
            assertEquals(1, tokens.size());
            assertEquals("DOUBLE", tokens.get(0).tag().value());
            assertEquals("155.0000000", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("print");
            assertEquals(1, tokens.size());
            assertEquals("PRINT", tokens.get(0).tag().value());
            assertEquals("print", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("size");
            assertEquals(1, tokens.size());
            assertEquals("SIZE", tokens.get(0).tag().value());
            assertEquals("size", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("else");
            assertEquals(1, tokens.size());
            assertEquals("ELSE", tokens.get(0).tag().value());
            assertEquals("else", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("new");
            assertEquals(1, tokens.size());
            assertEquals("NEW", tokens.get(0).tag().value());
            assertEquals("new", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("typeof");
            assertEquals(1, tokens.size());
            assertEquals("TYPEOF", tokens.get(0).tag().value());
            assertEquals("typeof", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("arraylist");
            assertEquals(1, tokens.size());
            assertEquals("ARRAYLIST", tokens.get(0).tag().value());
            assertEquals("arraylist", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("hashset");
            assertEquals(1, tokens.size());
            assertEquals("HASHSET", tokens.get(0).tag().value());
            assertEquals("hashset", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("get");
            assertEquals(1, tokens.size());
            assertEquals("GET", tokens.get(0).tag().value());
            assertEquals("get", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("put");
            assertEquals(1, tokens.size());
            assertEquals("PUT", tokens.get(0).tag().value());
            assertEquals("put", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("remove");
            assertEquals(1, tokens.size());
            assertEquals("REMOVE", tokens.get(0).tag().value());
            assertEquals("remove", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("rewrite");
            assertEquals(1, tokens.size());
            assertEquals("REWRITE", tokens.get(0).tag().value());
            assertEquals("rewrite", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze(",");
            assertEquals(1, tokens.size());
            assertEquals("COMMA", tokens.get(0).tag().value());
            assertEquals(",", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("\"string\"");
            assertEquals(1, tokens.size());
            assertEquals("STRING", tokens.get(0).tag().value());
            assertEquals("\"string\"", tokens.get(0).value());
        }
    }

    @Test(expected = RuntimeException.class)
    public void unexpected_symbol() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
            }
        }, new SymbolListenerImpl());

        try {
            lexer.analyze("unexpected");
        } catch (UnexpectedSymbolException e) {
            assertEquals("Unexpected symbol: u ( line 1, column 1 )", e.getMessage());
            throw e;
        }
    }

    @Test
    public void without_spaces() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
                tokenize("C").pattern("C");
            }
        }.build(), new SymbolListenerImpl());

        final Stream<Token> tokens = lexer.stream("ABC");
        assertEquals("A", tokens.next().tag().value());
        assertEquals("B", tokens.next().tag().value());
        assertEquals("C", tokens.next().tag().value());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void string_test() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("PRINT").pattern("^print$").priority(1);
                tokenize("LB").pattern("^\\($");
                tokenize("STRING").pattern("^\"[^\"]*\"$");
                tokenize("RB").pattern("^\\)$");
                tokenize("DEL").pattern("^;$");
            }
        }.build(), new SymbolListenerImpl());
        final Stream<Token> stream = lexer.stream("print(\"Test String\");");
        assertEquals("print", stream.next().value());
        assertEquals("(", stream.next().value());
        assertEquals("\"Test String\"", stream.next().value());
        assertEquals(")", stream.next().value());
        assertEquals(";", stream.next().value());
    }
}
