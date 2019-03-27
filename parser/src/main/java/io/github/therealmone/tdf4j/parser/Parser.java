package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.commons.Stream;
import io.github.therealmone.tdf4j.commons.Token;
import io.github.therealmone.tdf4j.parser.model.ast.AST;

import java.util.List;

public interface Parser {
    AST parse(final Stream<Token> tokens);

    AST parse(final List<Token> tokens);
}
