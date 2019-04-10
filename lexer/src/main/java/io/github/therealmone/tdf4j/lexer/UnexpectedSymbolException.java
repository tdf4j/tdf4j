package io.github.therealmone.tdf4j.lexer;

public class UnexpectedSymbolException extends RuntimeException {
    public UnexpectedSymbolException(final char symbol) {
        super("Unexpected symbol: " + symbol);
    }

    public UnexpectedSymbolException(final char symbol, final int line, final int column) {
        super("Unexpected symbol: " + symbol + " ( line " + line + ", column " + column + " )");
    }
}
