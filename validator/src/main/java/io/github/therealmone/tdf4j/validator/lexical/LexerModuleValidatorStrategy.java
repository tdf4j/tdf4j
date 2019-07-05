/*
 * Copyright (c) 2019 Roman Fatnev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.therealmone.tdf4j.validator.lexical;

import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.validator.ValidatorException;
import io.github.therealmone.tdf4j.validator.ValidatorRule;
import io.github.therealmone.tdf4j.validator.ValidatorStrategy;

import java.util.*;

public class LexerModuleValidatorStrategy implements ValidatorStrategy<AbstractLexerModule> {
    private final List<ValidatorRule<AbstractLexerModule>> rules;

    public LexerModuleValidatorStrategy() {
        this.rules = List.of(
                new TerminalListNotNullRule(),
                new TerminalTagNotNullRule(),
                new TerminalTagValueNotNull(),
                new TerminalPatternNullRule(),
                new TerminalCollisionRule()
        );
    }

    @Override
    public void apply(final AbstractLexerModule module) throws ValidatorException {
        if(!module.isBuilt()) {
            module.build();
        }
        for(final ValidatorRule<AbstractLexerModule> rule : rules) {
            rule.visit(module);
        }
    }

}
