package io.github.therealmone.tdf4j.validator.syntax;

import io.github.therealmone.tdf4j.validator.ValidatorException;

class ParserValidatorException extends ValidatorException {
    static ParserValidatorException PARSER_GRAMMAR_NULL = new ParserValidatorException("");

    private ParserValidatorException(final String message) {
        super(message);
    }
}
