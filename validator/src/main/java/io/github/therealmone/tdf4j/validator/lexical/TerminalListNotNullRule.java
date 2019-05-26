package io.github.therealmone.tdf4j.validator.lexical;

import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.validator.ValidatorException;
import io.github.therealmone.tdf4j.validator.ValidatorRule;

import static io.github.therealmone.tdf4j.validator.lexical.LexerValidatorException.LEXER_TERMINALS_NULL;

public class TerminalListNotNullRule implements ValidatorRule<AbstractLexerModule> {

    @Override
    public void visit(final AbstractLexerModule module) throws ValidatorException {
        if(module.getTerminals() == null) {
            throw LEXER_TERMINALS_NULL;
        }
    }

}
