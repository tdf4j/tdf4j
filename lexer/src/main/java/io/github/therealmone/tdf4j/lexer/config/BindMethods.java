package io.github.therealmone.tdf4j.lexer.config;

import io.github.therealmone.tdf4j.commons.model.ebnf.Terminal;

public interface BindMethods {
    Terminal.Builder tokenize(final String tag);
}
