/*
 * Copyright 2019 Roman Fatnev
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
package io.github.therealmone.tdf4j.validator.syntax;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.validator.ValidatorException;
import io.github.therealmone.tdf4j.validator.ValidatorRule;
import io.github.therealmone.tdf4j.validator.ValidatorStrategy;

import java.util.List;

public class ParserModuleValidatorStrategy implements ValidatorStrategy<AbstractParserModule> {
    private final List<ValidatorRule<AbstractParserModule>> rules;

    public ParserModuleValidatorStrategy() {
        this.rules = List.of(

        );
    }

    @Override
    public void apply(final AbstractParserModule module) throws ValidatorException {
        if(!module.isBuilt()) {
            module.build();
        }
        for(final ValidatorRule<AbstractParserModule> rule : rules) {
            rule.visit(module);
        }
    }

}