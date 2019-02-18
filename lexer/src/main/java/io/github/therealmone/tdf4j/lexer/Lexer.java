package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.commons.Token;

import java.util.List;

public interface Lexer {
    List<Token> analyze(final String input);
}
