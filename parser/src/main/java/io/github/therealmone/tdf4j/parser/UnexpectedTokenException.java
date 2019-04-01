package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.commons.Token;

public class UnexpectedTokenException extends RuntimeException {
    public UnexpectedTokenException(final Token token) {
        super("Unexpected token: " + token);
    }
}
