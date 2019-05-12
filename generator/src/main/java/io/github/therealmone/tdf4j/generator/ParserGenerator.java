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
package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.generator.impl.ParserGeneratorImpl;
import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;

public interface ParserGenerator extends Generator<Parser, AbstractParserModule> {

    <T extends Parser> T generate(final AbstractParserModule module, final Class<T> interfaceToImplement);

    static ParserGenerator newInstance() {
        return new ParserGeneratorImpl();
    }
}