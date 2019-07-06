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
