package io.github.therealmone.tdf4j.validator.impl;

import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.validator.Validator;
import io.github.therealmone.tdf4j.validator.ValidatorStrategy;
import io.github.therealmone.tdf4j.validator.lexical.LexerModuleValidatorStrategy;
import io.github.therealmone.tdf4j.validator.syntax.ParserModuleValidatorStrategy;
import org.junit.Test;


import static org.junit.Assert.*;

public class ValidatorFactoryTest {

    @Test
    public void parser_module() {
        final Validator<AbstractParserModule> validator = Validator.syntax();
        checkStrategy((ValidatorImpl) validator, ParserModuleValidatorStrategy.class);
    }

    @Test
    public void lexer_module() {
        final Validator<AbstractLexerModule> validator = Validator.lexical();
        checkStrategy((ValidatorImpl) validator, LexerModuleValidatorStrategy.class);
    }

    private void checkStrategy(final ValidatorImpl validator, final Class<? extends ValidatorStrategy> strategy) {
        assertTrue(strategy.isInstance(validator.strategy));
    }
}
