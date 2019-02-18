package io.github.therealmone.tdf4j.lexer.impl;

import io.github.therealmone.tdf4j.commons.Token;
import io.github.therealmone.tdf4j.lexer.AbstractLexerConfig;
import io.github.therealmone.tdf4j.lexer.Lexer;

import java.util.List;

public class LexerImpl implements Lexer {
    private final AbstractLexerConfig config;

    public LexerImpl(final AbstractLexerConfig config) {
        this.config = config;
    }

    @Override
    public List<Token> analyze(final String input) {
        throw new UnsupportedOperationException("todo");
    }
}
