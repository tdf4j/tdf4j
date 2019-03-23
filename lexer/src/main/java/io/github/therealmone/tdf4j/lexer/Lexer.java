package io.github.therealmone.tdf4j.lexer;

import io.github.therealmone.tdf4j.commons.Stream;
import io.github.therealmone.tdf4j.commons.bean.Token;

import javax.annotation.Nonnull;
import java.util.List;

public interface Lexer {
    @Nonnull
    List<Token> analyze(final String input);

    @Nonnull
    Stream<Token> stream(final String input);
}
