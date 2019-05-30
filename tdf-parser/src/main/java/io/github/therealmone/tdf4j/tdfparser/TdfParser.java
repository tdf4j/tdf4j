package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;

public interface TdfParser extends Parser {

    AbstractLexerModule getLexerModule();

    AbstractParserModule getParserModule();

}
