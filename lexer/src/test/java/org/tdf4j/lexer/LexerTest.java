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

package org.tdf4j.lexer;

import org.tdf4j.model.Stream;
import org.tdf4j.model.Token;
import org.tdf4j.lexer.impl.LexerImpl;
import org.tdf4j.lexer.impl.SymbolListenerImpl;
import org.tdf4j.lexer.utils.Config;
import org.tdf4j.module.lexer.AbstractLexerModule;
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
            assertEquals("VAR", tokens.get(0).getTag().getValue());
            assertEquals("value", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("0");
            assertEquals(1, tokens.size());
            assertEquals("DIGIT", tokens.get(0).getTag().getValue());
            assertEquals("0", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("=");
            assertEquals(1, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).getTag().getValue());
            assertEquals("=", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("+");
            assertEquals(1, tokens.size());
            assertEquals("OP", tokens.get(0).getTag().getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("value = 15 + 0");
            assertEquals(5, tokens.size());
            assertEquals("VAR", tokens.get(0).getTag().getValue());
            assertEquals("value", tokens.get(0).getValue());
            assertEquals("ASSIGN_OP", tokens.get(1).getTag().getValue());
            assertEquals("=", tokens.get(1).getValue());
            assertEquals("DIGIT", tokens.get(2).getTag().getValue());
            assertEquals("15", tokens.get(2).getValue());
            assertEquals("OP", tokens.get(3).getTag().getValue());
            assertEquals("+", tokens.get(3).getValue());
            assertEquals("DIGIT", tokens.get(4).getTag().getValue());
            assertEquals("0", tokens.get(4).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("15 = value - 10");
            assertEquals(5, tokens.size());
            assertEquals("DIGIT", tokens.get(0).getTag().getValue());
            assertEquals("15", tokens.get(0).getValue());
            assertEquals("ASSIGN_OP", tokens.get(1).getTag().getValue());
            assertEquals("=", tokens.get(1).getValue());
            assertEquals("VAR", tokens.get(2).getTag().getValue());
            assertEquals("value", tokens.get(2).getValue());
            assertEquals("OP", tokens.get(3).getTag().getValue());
            assertEquals("-", tokens.get(3).getValue());
            assertEquals("DIGIT", tokens.get(4).getTag().getValue());
            assertEquals("10", tokens.get(4).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("= value / 0");
            assertEquals(4, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).getTag().getValue());
            assertEquals("=", tokens.get(0).getValue());
            assertEquals("VAR", tokens.get(1).getTag().getValue());
            assertEquals("value", tokens.get(1).getValue());
            assertEquals("OP", tokens.get(2).getTag().getValue());
            assertEquals("/", tokens.get(2).getValue());
            assertEquals("DIGIT", tokens.get(3).getTag().getValue());
            assertEquals("0", tokens.get(3).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("* = value - 100000");
            assertEquals(5, tokens.size());
            assertEquals("OP", tokens.get(0).getTag().getValue());
            assertEquals("*", tokens.get(0).getValue());
            assertEquals("ASSIGN_OP", tokens.get(1).getTag().getValue());
            assertEquals("=", tokens.get(1).getValue());
            assertEquals("VAR", tokens.get(2).getTag().getValue());
            assertEquals("value", tokens.get(2).getValue());
            assertEquals("OP", tokens.get(3).getTag().getValue());
            assertEquals("-", tokens.get(3).getValue());
            assertEquals("DIGIT", tokens.get(4).getTag().getValue());
            assertEquals("100000", tokens.get(4).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("value = a + b - c / 0 * 10045645 = value1 = value2 = - * + /");
            assertEquals(22, tokens.size());
            assertEquals("VAR", tokens.get(0).getTag().getValue());
            assertEquals("value", tokens.get(0).getValue());
            assertEquals("ASSIGN_OP", tokens.get(1).getTag().getValue());
            assertEquals("=", tokens.get(1).getValue());
            assertEquals("VAR", tokens.get(2).getTag().getValue());
            assertEquals("a", tokens.get(2).getValue());
            assertEquals("OP", tokens.get(3).getTag().getValue());
            assertEquals("+", tokens.get(3).getValue());
            assertEquals("VAR", tokens.get(4).getTag().getValue());
            assertEquals("b", tokens.get(4).getValue());
            assertEquals("OP", tokens.get(5).getTag().getValue());
            assertEquals("-", tokens.get(5).getValue());
            assertEquals("VAR", tokens.get(6).getTag().getValue());
            assertEquals("c", tokens.get(6).getValue());
            assertEquals("OP", tokens.get(7).getTag().getValue());
            assertEquals("/", tokens.get(7).getValue());
            assertEquals("DIGIT", tokens.get(8).getTag().getValue());
            assertEquals("0", tokens.get(8).getValue());
            assertEquals("OP", tokens.get(9).getTag().getValue());
            assertEquals("*", tokens.get(9).getValue());
            assertEquals("DIGIT", tokens.get(10).getTag().getValue());
            assertEquals("10045645", tokens.get(10).getValue());
            assertEquals("ASSIGN_OP", tokens.get(11).getTag().getValue());
            assertEquals("=", tokens.get(11).getValue());
            assertEquals("VAR", tokens.get(12).getTag().getValue());
            assertEquals("value", tokens.get(12).getValue());
            assertEquals("DIGIT", tokens.get(13).getTag().getValue());
            assertEquals("1", tokens.get(13).getValue());
            assertEquals("ASSIGN_OP", tokens.get(14).getTag().getValue());
            assertEquals("=", tokens.get(14).getValue());
            assertEquals("VAR", tokens.get(15).getTag().getValue());
            assertEquals("value", tokens.get(15).getValue());
            assertEquals("DIGIT", tokens.get(16).getTag().getValue());
            assertEquals("2", tokens.get(16).getValue());
            assertEquals("ASSIGN_OP", tokens.get(17).getTag().getValue());
            assertEquals("=", tokens.get(17).getValue());
            assertEquals("OP", tokens.get(18).getTag().getValue());
            assertEquals("-", tokens.get(18).getValue());
            assertEquals("OP", tokens.get(19).getTag().getValue());
            assertEquals("*", tokens.get(19).getValue());
            assertEquals("OP", tokens.get(20).getTag().getValue());
            assertEquals("+", tokens.get(20).getValue());
            assertEquals("OP", tokens.get(21).getTag().getValue());
            assertEquals("/", tokens.get(21).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("= - * + / value1 = value2 = a + b * 0 - 9999 = 10000 - 10000 * = value");
            assertEquals(25, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).getTag().getValue());
            assertEquals("=", tokens.get(0).getValue());
            assertEquals("OP", tokens.get(1).getTag().getValue());
            assertEquals("-", tokens.get(1).getValue());
            assertEquals("OP", tokens.get(2).getTag().getValue());
            assertEquals("*", tokens.get(2).getValue());
            assertEquals("OP", tokens.get(3).getTag().getValue());
            assertEquals("+", tokens.get(3).getValue());
            assertEquals("OP", tokens.get(4).getTag().getValue());
            assertEquals("/", tokens.get(4).getValue());
            assertEquals("VAR", tokens.get(5).getTag().getValue());
            assertEquals("value", tokens.get(5).getValue());
            assertEquals("DIGIT", tokens.get(6).getTag().getValue());
            assertEquals("1", tokens.get(6).getValue());
            assertEquals("ASSIGN_OP", tokens.get(7).getTag().getValue());
            assertEquals("=", tokens.get(7).getValue());
            assertEquals("VAR", tokens.get(8).getTag().getValue());
            assertEquals("value", tokens.get(8).getValue());
            assertEquals("DIGIT", tokens.get(9).getTag().getValue());
            assertEquals("2", tokens.get(9).getValue());
            assertEquals("ASSIGN_OP", tokens.get(10).getTag().getValue());
            assertEquals("=", tokens.get(10).getValue());
            assertEquals("VAR", tokens.get(11).getTag().getValue());
            assertEquals("a", tokens.get(11).getValue());
            assertEquals("OP", tokens.get(12).getTag().getValue());
            assertEquals("+", tokens.get(12).getValue());
            assertEquals("VAR", tokens.get(13).getTag().getValue());
            assertEquals("b", tokens.get(13).getValue());
            assertEquals("OP", tokens.get(14).getTag().getValue());
            assertEquals("*", tokens.get(14).getValue());
            assertEquals("DIGIT", tokens.get(15).getTag().getValue());
            assertEquals("0", tokens.get(15).getValue());
            assertEquals("OP", tokens.get(16).getTag().getValue());
            assertEquals("-", tokens.get(16).getValue());
            assertEquals("DIGIT", tokens.get(17).getTag().getValue());
            assertEquals("9999", tokens.get(17).getValue());
            assertEquals("ASSIGN_OP", tokens.get(18).getTag().getValue());
            assertEquals("=", tokens.get(18).getValue());
            assertEquals("DIGIT", tokens.get(19).getTag().getValue());
            assertEquals("10000", tokens.get(19).getValue());
            assertEquals("OP", tokens.get(20).getTag().getValue());
            assertEquals("-", tokens.get(20).getValue());
            assertEquals("DIGIT", tokens.get(21).getTag().getValue());
            assertEquals("10000", tokens.get(21).getValue());
            assertEquals("OP", tokens.get(22).getTag().getValue());
            assertEquals("*", tokens.get(22).getValue());
            assertEquals("ASSIGN_OP", tokens.get(23).getTag().getValue());
            assertEquals("=", tokens.get(23).getValue());
            assertEquals("VAR", tokens.get(24).getTag().getValue());
            assertEquals("value", tokens.get(24).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("= = = = = = = = = = = 10000 100 10 1 0 v a l u e * * * * - - - - / / /");
            assertEquals(32, tokens.size());
            assertEquals("ASSIGN_OP", tokens.get(0).getTag().getValue());
            assertEquals("=", tokens.get(0).getValue());
            assertEquals("ASSIGN_OP", tokens.get(1).getTag().getValue());
            assertEquals("=", tokens.get(1).getValue());
            assertEquals("ASSIGN_OP", tokens.get(2).getTag().getValue());
            assertEquals("=", tokens.get(2).getValue());
            assertEquals("ASSIGN_OP", tokens.get(3).getTag().getValue());
            assertEquals("=", tokens.get(3).getValue());
            assertEquals("ASSIGN_OP", tokens.get(4).getTag().getValue());
            assertEquals("=", tokens.get(4).getValue());
            assertEquals("ASSIGN_OP", tokens.get(5).getTag().getValue());
            assertEquals("=", tokens.get(5).getValue());
            assertEquals("ASSIGN_OP", tokens.get(6).getTag().getValue());
            assertEquals("=", tokens.get(6).getValue());
            assertEquals("ASSIGN_OP", tokens.get(7).getTag().getValue());
            assertEquals("=", tokens.get(7).getValue());
            assertEquals("ASSIGN_OP", tokens.get(8).getTag().getValue());
            assertEquals("=", tokens.get(8).getValue());
            assertEquals("ASSIGN_OP", tokens.get(9).getTag().getValue());
            assertEquals("=", tokens.get(9).getValue());
            assertEquals("ASSIGN_OP", tokens.get(10).getTag().getValue());
            assertEquals("=", tokens.get(10).getValue());
            assertEquals("DIGIT", tokens.get(11).getTag().getValue());
            assertEquals("10000", tokens.get(11).getValue());
            assertEquals("DIGIT", tokens.get(12).getTag().getValue());
            assertEquals("100", tokens.get(12).getValue());
            assertEquals("DIGIT", tokens.get(13).getTag().getValue());
            assertEquals("10", tokens.get(13).getValue());
            assertEquals("DIGIT", tokens.get(14).getTag().getValue());
            assertEquals("1", tokens.get(14).getValue());
            assertEquals("DIGIT", tokens.get(15).getTag().getValue());
            assertEquals("0", tokens.get(15).getValue());
            assertEquals("VAR", tokens.get(16).getTag().getValue());
            assertEquals("v", tokens.get(16).getValue());
            assertEquals("VAR", tokens.get(17).getTag().getValue());
            assertEquals("a", tokens.get(17).getValue());
            assertEquals("VAR", tokens.get(18).getTag().getValue());
            assertEquals("l", tokens.get(18).getValue());
            assertEquals("VAR", tokens.get(19).getTag().getValue());
            assertEquals("u", tokens.get(19).getValue());
            assertEquals("VAR", tokens.get(20).getTag().getValue());
            assertEquals("e", tokens.get(20).getValue());
            assertEquals("OP", tokens.get(21).getTag().getValue());
            assertEquals("*", tokens.get(21).getValue());
            assertEquals("OP", tokens.get(22).getTag().getValue());
            assertEquals("*", tokens.get(22).getValue());
            assertEquals("OP", tokens.get(23).getTag().getValue());
            assertEquals("*", tokens.get(23).getValue());
            assertEquals("OP", tokens.get(24).getTag().getValue());
            assertEquals("*", tokens.get(24).getValue());
            assertEquals("OP", tokens.get(25).getTag().getValue());
            assertEquals("-", tokens.get(25).getValue());
            assertEquals("OP", tokens.get(26).getTag().getValue());
            assertEquals("-", tokens.get(26).getValue());
            assertEquals("OP", tokens.get(27).getTag().getValue());
            assertEquals("-", tokens.get(27).getValue());
            assertEquals("OP", tokens.get(28).getTag().getValue());
            assertEquals("-", tokens.get(28).getValue());
            assertEquals("OP", tokens.get(29).getTag().getValue());
            assertEquals("/", tokens.get(29).getValue());
            assertEquals("OP", tokens.get(30).getTag().getValue());
            assertEquals("/", tokens.get(30).getValue());
            assertEquals("OP", tokens.get(31).getTag().getValue());
            assertEquals("/", tokens.get(31).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("!= !");
            assertEquals(2, tokens.size());
            assertEquals("COP", tokens.get(0).getTag().getValue());
            assertEquals("!=", tokens.get(0).getValue());
            assertEquals("LOP", tokens.get(1).getTag().getValue());
            assertEquals("!", tokens.get(1).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("0.155");
            assertEquals(1, tokens.size());
            assertEquals("DOUBLE", tokens.get(0).getTag().getValue());
            assertEquals("0.155", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("155.0000000");
            assertEquals(1, tokens.size());
            assertEquals("DOUBLE", tokens.get(0).getTag().getValue());
            assertEquals("155.0000000", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("print");
            assertEquals(1, tokens.size());
            assertEquals("PRINT", tokens.get(0).getTag().getValue());
            assertEquals("print", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("size");
            assertEquals(1, tokens.size());
            assertEquals("SIZE", tokens.get(0).getTag().getValue());
            assertEquals("size", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("else");
            assertEquals(1, tokens.size());
            assertEquals("ELSE", tokens.get(0).getTag().getValue());
            assertEquals("else", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("new");
            assertEquals(1, tokens.size());
            assertEquals("NEW", tokens.get(0).getTag().getValue());
            assertEquals("new", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("typeof");
            assertEquals(1, tokens.size());
            assertEquals("TYPEOF", tokens.get(0).getTag().getValue());
            assertEquals("typeof", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("arraylist");
            assertEquals(1, tokens.size());
            assertEquals("ARRAYLIST", tokens.get(0).getTag().getValue());
            assertEquals("arraylist", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("hashset");
            assertEquals(1, tokens.size());
            assertEquals("HASHSET", tokens.get(0).getTag().getValue());
            assertEquals("hashset", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("get");
            assertEquals(1, tokens.size());
            assertEquals("GET", tokens.get(0).getTag().getValue());
            assertEquals("get", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("put");
            assertEquals(1, tokens.size());
            assertEquals("PUT", tokens.get(0).getTag().getValue());
            assertEquals("put", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("remove");
            assertEquals(1, tokens.size());
            assertEquals("REMOVE", tokens.get(0).getTag().getValue());
            assertEquals("remove", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("rewrite");
            assertEquals(1, tokens.size());
            assertEquals("REWRITE", tokens.get(0).getTag().getValue());
            assertEquals("rewrite", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze(",");
            assertEquals(1, tokens.size());
            assertEquals("COMMA", tokens.get(0).getTag().getValue());
            assertEquals(",", tokens.get(0).getValue());
        }

        {
            final List<Token> tokens = lexer.analyze("\"string\"");
            assertEquals(1, tokens.size());
            assertEquals("STRING", tokens.get(0).getTag().getValue());
            assertEquals("\"string\"", tokens.get(0).getValue());
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
        assertEquals("A", tokens.next().getTag().getValue());
        assertEquals("B", tokens.next().getTag().getValue());
        assertEquals("C", tokens.next().getTag().getValue());
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
        assertEquals("print", stream.next().getValue());
        assertEquals("(", stream.next().getValue());
        assertEquals("\"Test String\"", stream.next().getValue());
        assertEquals(")", stream.next().getValue());
        assertEquals(";", stream.next().getValue());
    }
}
