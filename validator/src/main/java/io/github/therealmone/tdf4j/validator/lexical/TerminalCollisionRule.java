package io.github.therealmone.tdf4j.validator.lexical;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.validator.ValidatorException;
import io.github.therealmone.tdf4j.validator.ValidatorRule;

import java.util.HashSet;
import java.util.Set;

import static io.github.therealmone.tdf4j.validator.lexical.LexerValidatorException.LEXER_TERMINAL_COLLISION;

public class TerminalCollisionRule implements ValidatorRule<AbstractLexerModule> {

    @Override
    public void visit(final AbstractLexerModule module) throws ValidatorException {
        final Set<String> tags = new HashSet<>();
        for(final Terminal terminal : module.getTerminals()) {
            if(tags.contains(terminal.getTag().getValue())) {
                throw LEXER_TERMINAL_COLLISION;
            }
            tags.add(terminal.getTag().getValue());
        }
    }

}
