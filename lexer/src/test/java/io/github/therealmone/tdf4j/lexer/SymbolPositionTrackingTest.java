package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.lexer.config.AbstractLexerModule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SymbolPositionTrackingTest {
    private final LexerFactory factory = new LexerFactory();

    @Test
    public void first_symbol_at_first_line() {
        final Lexer lexer = factory.withModule(new AbstractLexerModule() {
            @Override
            public void configure() {
            }
        });
        assertThrows(() -> lexer.analyze("A\nB\nC"), UnexpectedSymbolException.class, unexpectedSymbol('A', 1, 1));
    }

    @Test
    public void last_symbol_at_first_line() {
        final Lexer lexer = factory.withModule(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
            }
        });
        assertThrows(() -> lexer.analyze("ABC\nA\nB"), UnexpectedSymbolException.class, unexpectedSymbol('C', 1, 3));
    }

    @Test
    public void first_symbol_at_last_line() {
        final Lexer lexer = factory.withModule(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
            }
        });
        assertThrows(() -> lexer.analyze("A \n B \n C"), UnexpectedSymbolException.class, unexpectedSymbol('C', 3, 2));
    }

    @Test
    public void last_symbol_at_last_line() {
        final Lexer lexer = factory.withModule(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
            }
        });
        assertThrows(() -> lexer.analyze("AA\n BBBA \n ABC"), UnexpectedSymbolException.class, unexpectedSymbol('C', 3, 4));
    }

    @Test
    public void middle() {
        final Lexer lexer = factory.withModule(new AbstractLexerModule() {
            @Override
            public void configure() {
                tokenize("A").pattern("A");
                tokenize("B").pattern("B");
            }
        });
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
