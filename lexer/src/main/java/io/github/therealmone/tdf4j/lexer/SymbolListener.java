package io.github.therealmone.tdf4j.lexer;

public interface SymbolListener {
    void listen(final char ch);

    int line();

    int column();

    void reset();
}
