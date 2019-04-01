package io.github.therealmone.tdf4j.lexer;

public class UnexpectedSymbolException extends RuntimeException {
    public UnexpectedSymbolException(final char symbol) {
        super("Unexpected symbol: " + symbol);
    }
}
