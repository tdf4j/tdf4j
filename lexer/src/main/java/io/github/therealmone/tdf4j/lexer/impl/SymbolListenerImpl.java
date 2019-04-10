package io.github.therealmone.tdf4j.lexer.impl;

import io.github.therealmone.tdf4j.lexer.SymbolListener;

public class SymbolListenerImpl implements SymbolListener {
    private int column = 0;
    private int line = 1;

    @Override
    public void listen(char ch) {
        if(newLine(ch)) {
            line++;
            column = 0;
        } else {
            column++;
        }
    }

    @Override
    public int line() {
        return line;
    }

    @Override
    public int column() {
        return column;
    }

    @Override
    public void reset() {
        this.line = 1;
        this.column = 0;
    }

    private boolean newLine(final char ch) {
        return ch == '\n';
    }
}
