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
package org.tdf4j.validator;

import org.tdf4j.core.module.Module;
import org.tdf4j.core.module.LexerAbstractModule;
import org.tdf4j.core.module.ParserAbstractModule;
import org.tdf4j.validator.impl.ValidatorImpl;
import org.tdf4j.validator.lexical.LexerModuleValidatorStrategy;
import org.tdf4j.validator.syntax.ParserModuleValidatorStrategy;

public interface Validator<T extends Module> {

    void validate(final T module) throws ValidatorException;

    static Validator<LexerAbstractModule> lexical() {
        return new ValidatorImpl<>(new LexerModuleValidatorStrategy());
    }

    static Validator<ParserAbstractModule> syntax() {
        return new ValidatorImpl<>(new ParserModuleValidatorStrategy());
    }
}
