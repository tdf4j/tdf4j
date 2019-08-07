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

import org.tdf4j.lexer.impl.LexerImpl;
import org.tdf4j.lexer.impl.SymbolListenerImpl;
import org.tdf4j.module.lexer.AbstractLexerModule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SymbolPositionTrackingTest {

    @Test
    public void first_symbol_at_first_line() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
            }
        }.build(), new SymbolListenerImpl());
        assertThrows(() -> lexer.analyze("A\nB\nC"), UnexpectedSymbolException.class, unexpectedSymbol('A', 1, 1));
    }

    @Test
    public void last_symbol_at_first_line() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
                tokenize("ws").pattern("\\s|\\n|\\r").hidden(true);
            }
        }.build(), new SymbolListenerImpl());
        assertThrows(() -> lexer.analyze("ABC\nA\nB"), UnexpectedSymbolException.class, unexpectedSymbol('C', 1, 3));
    }

    @Test
    public void first_symbol_at_last_line() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
                tokenize("ws").pattern("\\s|\\n|\\r").hidden(true);
            }
        }.build(), new SymbolListenerImpl());
        assertThrows(() -> lexer.analyze("A \n B \n C"), UnexpectedSymbolException.class, unexpectedSymbol('C', 3, 2));
    }

    @Test
    public void last_symbol_at_last_line() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
                tokenize("ws").pattern("\\s|\\n|\\r").hidden(true);
            }
        }.build(), new SymbolListenerImpl());
        assertThrows(() -> lexer.analyze("AA\n BBBA \n ABC"), UnexpectedSymbolException.class, unexpectedSymbol('C', 3, 4));
    }

    @Test
    public void middle() {
        final Lexer lexer = new LexerImpl(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
                tokenize("ws").pattern("\\s|\\n|\\r").hidden(true);
            }
        }.build(), new SymbolListenerImpl());
        assertThrows(() -> lexer.analyze("AA \nBBABCA\n ABC"), UnexpectedSymbolException.class, unexpectedSymbol('C', 2, 5));
    }

    private static void assertThrows(final Callback callback, final Class<? extends Throwable> ex, final String message) {
        try {
            callback.call();
            fail("Expected exception: " + ex.getName() + ": " + message);
        } catch (Exception e) {
            assertEquals(ex.getName(), e.getClass().getName());
            assertEquals(message, e.getMessage());
        }
    }

    @FunctionalInterface
    private interface Callback {
        void call();
    }

    private String unexpectedSymbol(final char ch, final int line, final int column) {
        return String.format("Unexpected symbol: %s ( line %d, column %d )", ch, line, column);
    }
}
