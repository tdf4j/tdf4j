package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.commons.bean.Token;
import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import io.github.therealmone.tdf4j.lexer.impl.LexerImpl;
import io.github.therealmone.tdf4j.lexer.utils.Config;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LexerTest {
    @Test
    //these are old tests from http://github.com/therealmone/SPOTranslator
    public void analyze() {
        Lexer lexer = new LexerImpl(new Config().build());

        {
            final List<Token> tokens = lexer.analyze("value");
            assertEquals(1, tokens.size());
            assertEquals("VAR", tokens.get(0).tag());
            assertEquals("value", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("0");
            assertEquals(1, tokens.size());
            assertEquals("DIGIT", tokens.get(0).tag());
            assertEquals("0", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("=");
            assertEquals(1, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag());
            assertEquals("=", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("+");
            assertEquals(1, tokens.size());
            assertEquals("OP", tokens.get(0).tag());
        }

        {
            final List<Token> tokens = lexer.analyze("value = 15 + 0");
            assertEquals(5, tokens.size());
            assertEquals("VAR", tokens.get(0).tag());
            assertEquals("value", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag());
            assertEquals("=", tokens.get(1).value());
            assertEquals("DIGIT", tokens.get(2).tag());
            assertEquals("15", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag());
            assertEquals("+", tokens.get(3).value());
            assertEquals("DIGIT", tokens.get(4).tag());
            assertEquals("0", tokens.get(4).value());
        }

        {
            final List<Token> tokens = lexer.analyze("15 = value - 10");
            assertEquals(5, tokens.size());
            assertEquals("DIGIT", tokens.get(0).tag());
            assertEquals("15", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag());
            assertEquals("=", tokens.get(1).value());
            assertEquals("VAR", tokens.get(2).tag());
            assertEquals("value", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag());
            assertEquals("-", tokens.get(3).value());
            assertEquals("DIGIT", tokens.get(4).tag());
            assertEquals("10", tokens.get(4).value());
        }

        {
            final List<Token> tokens = lexer.analyze("= value / 0");
            assertEquals(4, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag());
            assertEquals("=", tokens.get(0).value());
            assertEquals("VAR", tokens.get(1).tag());
            assertEquals("value", tokens.get(1).value());
            assertEquals("OP", tokens.get(2).tag());
            assertEquals("/", tokens.get(2).value());
            assertEquals("DIGIT", tokens.get(3).tag());
            assertEquals("0", tokens.get(3).value());
        }

        {
            final List<Token> tokens = lexer.analyze("* = value - 100000");
            assertEquals(5, tokens.size());
            assertEquals("OP", tokens.get(0).tag());
            assertEquals("*", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag());
            assertEquals("=", tokens.get(1).value());
            assertEquals("VAR", tokens.get(2).tag());
            assertEquals("value", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag());
            assertEquals("-", tokens.get(3).value());
            assertEquals("DIGIT", tokens.get(4).tag());
            assertEquals("100000", tokens.get(4).value());
        }

        {
            final List<Token> tokens = lexer.analyze("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /");
            assertEquals(22, tokens.size());
            assertEquals("VAR", tokens.get(0).tag());
            assertEquals("value", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag());
            assertEquals("=", tokens.get(1).value());
            assertEquals("VAR", tokens.get(2).tag());
            assertEquals("a", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag());
            assertEquals("+", tokens.get(3).value());
            assertEquals("VAR", tokens.get(4).tag());
            assertEquals("b", tokens.get(4).value());
            assertEquals("OP", tokens.get(5).tag());
            assertEquals("-", tokens.get(5).value());
            assertEquals("VAR", tokens.get(6).tag());
            assertEquals("c", tokens.get(6).value());
            assertEquals("OP", tokens.get(7).tag());
            assertEquals("/", tokens.get(7).value());
            assertEquals("DIGIT", tokens.get(8).tag());
            assertEquals("0", tokens.get(8).value());
            assertEquals("OP", tokens.get(9).tag());
            assertEquals("*", tokens.get(9).value());
            assertEquals("DIGIT", tokens.get(10).tag());
            assertEquals("10045645", tokens.get(10).value());
            assertEquals("ASSIGN_OP", tokens.get(11).tag());
            assertEquals("=", tokens.get(11).value());
            assertEquals("VAR", tokens.get(12).tag());
            assertEquals("value", tokens.get(12).value());
            assertEquals("DIGIT", tokens.get(13).tag());
            assertEquals("1", tokens.get(13).value());
            assertEquals("ASSIGN_OP", tokens.get(14).tag());
            assertEquals("=", tokens.get(14).value());
            assertEquals("VAR", tokens.get(15).tag());
            assertEquals("value", tokens.get(15).value());
            assertEquals("DIGIT", tokens.get(16).tag());
            assertEquals("2", tokens.get(16).value());
            assertEquals("ASSIGN_OP", tokens.get(17).tag());
            assertEquals("=", tokens.get(17).value());
            assertEquals("OP", tokens.get(18).tag());
            assertEquals("-", tokens.get(18).value());
            assertEquals("OP", tokens.get(19).tag());
            assertEquals("*", tokens.get(19).value());
            assertEquals("OP", tokens.get(20).tag());
            assertEquals("+", tokens.get(20).value());
            assertEquals("OP", tokens.get(21).tag());
            assertEquals("/", tokens.get(21).value());
        }

        {
            final List<Token> tokens = lexer.analyze("= - * + / value1 = value2 = a + b * 0 - 9999 = 10000 - 10000 * = value");
            assertEquals(25, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag());
            assertEquals("=", tokens.get(0).value());
            assertEquals("OP", tokens.get(1).tag());
            assertEquals("-", tokens.get(1).value());
            assertEquals("OP", tokens.get(2).tag());
            assertEquals("*", tokens.get(2).value());
            assertEquals("OP", tokens.get(3).tag());
            assertEquals("+", tokens.get(3).value());
            assertEquals("OP", tokens.get(4).tag());
            assertEquals("/", tokens.get(4).value());
            assertEquals("VAR", tokens.get(5).tag());
            assertEquals("value", tokens.get(5).value());
            assertEquals("DIGIT", tokens.get(6).tag());
            assertEquals("1", tokens.get(6).value());
            assertEquals("ASSIGN_OP", tokens.get(7).tag());
            assertEquals("=", tokens.get(7).value());
            assertEquals("VAR", tokens.get(8).tag());
            assertEquals("value", tokens.get(8).value());
            assertEquals("DIGIT", tokens.get(9).tag());
            assertEquals("2", tokens.get(9).value());
            assertEquals("ASSIGN_OP", tokens.get(10).tag());
            assertEquals("=", tokens.get(10).value());
            assertEquals("VAR", tokens.get(11).tag());
            assertEquals("a", tokens.get(11).value());
            assertEquals("OP", tokens.get(12).tag());
            assertEquals("+", tokens.get(12).value());
            assertEquals("VAR", tokens.get(13).tag());
            assertEquals("b", tokens.get(13).value());
            assertEquals("OP", tokens.get(14).tag());
            assertEquals("*", tokens.get(14).value());
            assertEquals("DIGIT", tokens.get(15).tag());
            assertEquals("0", tokens.get(15).value());
            assertEquals("OP", tokens.get(16).tag());
            assertEquals("-", tokens.get(16).value());
            assertEquals("DIGIT", tokens.get(17).tag());
            assertEquals("9999", tokens.get(17).value());
            assertEquals("ASSIGN_OP", tokens.get(18).tag());
            assertEquals("=", tokens.get(18).value());
            assertEquals("DIGIT", tokens.get(19).tag());
            assertEquals("10000", tokens.get(19).value());
            assertEquals("OP", tokens.get(20).tag());
            assertEquals("-", tokens.get(20).value());
            assertEquals("DIGIT", tokens.get(21).tag());
            assertEquals("10000", tokens.get(21).value());
            assertEquals("OP", tokens.get(22).tag());
            assertEquals("*", tokens.get(22).value());
            assertEquals("ASSIGN_OP", tokens.get(23).tag());
            assertEquals("=", tokens.get(23).value());
            assertEquals("VAR", tokens.get(24).tag());
            assertEquals("value", tokens.get(24).value());
        }

        {
            final List<Token> tokens = lexer.analyze("= = = = = = = = = = = 10000 100 10 1 0 v a l u e * * * * - - - - / / /");
            assertEquals(32, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).tag());
            assertEquals("=", tokens.get(0).value());
            assertEquals("ASSIGN_OP", tokens.get(1).tag());
            assertEquals("=", tokens.get(1).value());
            assertEquals("ASSIGN_OP", tokens.get(2).tag());
            assertEquals("=", tokens.get(2).value());
            assertEquals("ASSIGN_OP", tokens.get(3).tag());
            assertEquals("=", tokens.get(3).value());
            assertEquals("ASSIGN_OP", tokens.get(4).tag());
            assertEquals("=", tokens.get(4).value());
            assertEquals("ASSIGN_OP", tokens.get(5).tag());
            assertEquals("=", tokens.get(5).value());
            assertEquals("ASSIGN_OP", tokens.get(6).tag());
            assertEquals("=", tokens.get(6).value());
            assertEquals("ASSIGN_OP", tokens.get(7).tag());
            assertEquals("=", tokens.get(7).value());
            assertEquals("ASSIGN_OP", tokens.get(8).tag());
            assertEquals("=", tokens.get(8).value());
            assertEquals("ASSIGN_OP", tokens.get(9).tag());
            assertEquals("=", tokens.get(9).value());
            assertEquals("ASSIGN_OP", tokens.get(10).tag());
            assertEquals("=", tokens.get(10).value());
            assertEquals("DIGIT", tokens.get(11).tag());
            assertEquals("10000", tokens.get(11).value());
            assertEquals("DIGIT", tokens.get(12).tag());
            assertEquals("100", tokens.get(12).value());
            assertEquals("DIGIT", tokens.get(13).tag());
            assertEquals("10", tokens.get(13).value());
            assertEquals("DIGIT", tokens.get(14).tag());
            assertEquals("1", tokens.get(14).value());
            assertEquals("DIGIT", tokens.get(15).tag());
            assertEquals("0", tokens.get(15).value());
            assertEquals("VAR", tokens.get(16).tag());
            assertEquals("v", tokens.get(16).value());
            assertEquals("VAR", tokens.get(17).tag());
            assertEquals("a", tokens.get(17).value());
            assertEquals("VAR", tokens.get(18).tag());
            assertEquals("l", tokens.get(18).value());
            assertEquals("VAR", tokens.get(19).tag());
            assertEquals("u", tokens.get(19).value());
            assertEquals("VAR", tokens.get(20).tag());
            assertEquals("e", tokens.get(20).value());
            assertEquals("OP", tokens.get(21).tag());
            assertEquals("*", tokens.get(21).value());
            assertEquals("OP", tokens.get(22).tag());
            assertEquals("*", tokens.get(22).value());
            assertEquals("OP", tokens.get(23).tag());
            assertEquals("*", tokens.get(23).value());
            assertEquals("OP", tokens.get(24).tag());
            assertEquals("*", tokens.get(24).value());
            assertEquals("OP", tokens.get(25).tag());
            assertEquals("-", tokens.get(25).value());
            assertEquals("OP", tokens.get(26).tag());
            assertEquals("-", tokens.get(26).value());
            assertEquals("OP", tokens.get(27).tag());
            assertEquals("-", tokens.get(27).value());
            assertEquals("OP", tokens.get(28).tag());
            assertEquals("-", tokens.get(28).value());
            assertEquals("OP", tokens.get(29).tag());
            assertEquals("/", tokens.get(29).value());
            assertEquals("OP", tokens.get(30).tag());
            assertEquals("/", tokens.get(30).value());
            assertEquals("OP", tokens.get(31).tag());
            assertEquals("/", tokens.get(31).value());
        }

        {
            final List<Token> tokens = lexer.analyze("!= !");
            assertEquals(2, tokens.size());
            assertEquals("COP", tokens.get(0).tag());
            assertEquals("!=", tokens.get(0).value());
            assertEquals("LOP", tokens.get(1).tag());
            assertEquals("!", tokens.get(1).value());
        }

        {
            final List<Token> tokens = lexer.analyze("0.155");
            assertEquals(1, tokens.size());
            assertEquals("DOUBLE", tokens.get(0).tag());
            assertEquals("0.155", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("155.0000000");
            assertEquals(1, tokens.size());
            assertEquals("DOUBLE", tokens.get(0).tag());
            assertEquals("155.0000000", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("print");
            assertEquals(1, tokens.size());
            assertEquals("PRINT", tokens.get(0).tag());
            assertEquals("print", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("size");
            assertEquals(1, tokens.size());
            assertEquals("SIZE", tokens.get(0).tag());
            assertEquals("size", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("else");
            assertEquals(1, tokens.size());
            assertEquals("ELSE", tokens.get(0).tag());
            assertEquals("else", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("new");
            assertEquals(1, tokens.size());
            assertEquals("NEW", tokens.get(0).tag());
            assertEquals("new", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("typeof");
            assertEquals(1, tokens.size());
            assertEquals("TYPEOF", tokens.get(0).tag());
            assertEquals("typeof", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("arraylist");
            assertEquals(1, tokens.size());
            assertEquals("ARRAYLIST", tokens.get(0).tag());
            assertEquals("arraylist", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("hashset");
            assertEquals(1, tokens.size());
            assertEquals("HASHSET", tokens.get(0).tag());
            assertEquals("hashset", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("get");
            assertEquals(1, tokens.size());
            assertEquals("GET", tokens.get(0).tag());
            assertEquals("get", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("put");
            assertEquals(1, tokens.size());
            assertEquals("PUT", tokens.get(0).tag());
            assertEquals("put", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("remove");
            assertEquals(1, tokens.size());
            assertEquals("REMOVE", tokens.get(0).tag());
            assertEquals("remove", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("rewrite");
            assertEquals(1, tokens.size());
            assertEquals("REWRITE", tokens.get(0).tag());
            assertEquals("rewrite", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze(",");
            assertEquals(1, tokens.size());
            assertEquals("COMMA", tokens.get(0).tag());
            assertEquals(",", tokens.get(0).value());
        }

        {
            final List<Token> tokens = lexer.analyze("\"string\"");
            assertEquals(1, tokens.size());
            assertEquals("STRING", tokens.get(0).tag());
            assertEquals("\"string\"", tokens.get(0).value());
        }
    }

    @Test(expected = RuntimeException.class)
    public void unexpected_symbol() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
            }
        });

        try {
            lexer.analyze("unexpected");
        } catch (RuntimeException e) {
            assertEquals("Unexpected symbol: u", e.getMessage());
            throw e;
        }
    }
}
