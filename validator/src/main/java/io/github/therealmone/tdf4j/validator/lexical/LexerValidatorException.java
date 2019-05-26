package io.github.therealmone.tdf4j.validator.lexical;

import io.github.therealmone.tdf4j.validator.ValidatorException;

class LexerValidatorException extends ValidatorException {
    static final LexerValidatorException LEXER_TERMINALS_NULL = new LexerValidatorException("Terminal list can't be null");
    static final LexerValidatorException LEXER_TERMINAL_TAG_NULL = new LexerValidatorException("Terminal tag can't be null");
    static final LexerValidatorException LEXER_TERMINAL_TAG_VALUE_NULL = new LexerValidatorException("Terminal tag value can't be null");
    static final LexerValidatorException LEXER_TERMINAL_PATTERN_NULL = new LexerValidatorException("Terminal pattern can't be null");
    static final LexerValidatorException LEXER_TERMINAL_COLLISION = new LexerValidatorException("Terminal tag must be unique");

    private LexerValidatorException(final String message) {
        super(message);
    }

}
