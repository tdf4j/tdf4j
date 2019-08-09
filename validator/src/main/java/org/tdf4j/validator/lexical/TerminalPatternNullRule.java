/*
 * Copyright (c) 2019 tdf4j
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

package org.tdf4j.validator.lexical;

import org.tdf4j.core.model.ebnf.Terminal;
import org.tdf4j.core.module.LexerAbstractModule;
import org.tdf4j.validator.ValidatorException;
import org.tdf4j.validator.ValidatorRule;

import static org.tdf4j.validator.lexical.LexerValidatorException.LEXER_TERMINAL_PATTERN_NULL;

public class TerminalPatternNullRule implements ValidatorRule<LexerAbstractModule> {

    @Override
    public void visit(final LexerAbstractModule module) throws ValidatorException {
        for(final Terminal terminal : module.getTerminals()) {
            if(terminal.getPattern() == null) {
                throw LEXER_TERMINAL_PATTERN_NULL;
            }
        }
    }

}