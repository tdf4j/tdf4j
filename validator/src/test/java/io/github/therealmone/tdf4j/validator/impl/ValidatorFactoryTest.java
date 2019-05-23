package io.github.therealmone.tdf4j.validator.impl;

import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.validator.Validator;
import io.github.therealmone.tdf4j.validator.ValidatorFactory;
import io.github.therealmone.tdf4j.validator.ValidatorNotFoundException;
import io.github.therealmone.tdf4j.validator.ValidatorStrategy;
import io.github.therealmone.tdf4j.validator.strategies.LexerModuleValidatorStrategy;
import io.github.therealmone.tdf4j.validator.strategies.ParserModuleValidatorStrategy;
import org.junit.Test;


import static org.junit.Assert.*;

public class ValidatorFactoryTest {

    @Test
    public void parser_module() throws ValidatorNotFoundException {
        final Validator<AbstractParserModule> validator = ValidatorFactory.get(new AbstractParserModule() {
            @Override
            public void configure() {
            }
        });
        checkStrategy((ValidatorImpl) validator, ParserModuleValidatorStrategy.class);
    }

    @Test
    public void lexer_module() throws ValidatorNotFoundException {
        final Validator validator = ValidatorFactory.get(new AbstractLexerModule() {
            @Override
            public void configure() {
            }
        });
        checkStrategy((ValidatorImpl) validator, LexerModuleValidatorStrategy.class);
    }


    @Test(expected = ValidatorNotFoundException.class)
    public void validator_not_found() throws ValidatorNotFoundException {
        ValidatorFactory.get(() -> {});
    }

    private void checkStrategy(final ValidatorImpl validator, final Class<? extends ValidatorStrategy> strategy) {
        assertTrue(strategy.isInstance(validator.strategy));
    }
}
