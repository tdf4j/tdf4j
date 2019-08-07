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
package org.tdf4j.generator.impl;

import org.tdf4j.generator.Generator;
import org.tdf4j.generator.LexerOptions;
import org.tdf4j.lexer.Lexer;
import org.tdf4j.lexer.impl.LexerImpl;

public class LexerGenerator implements Generator<Lexer> {
    private final LexerOptions options;

    public LexerGenerator(final LexerOptions options) {
        this.options = options;
        options.getModule().build();
    }

    @Override
    public Lexer generate() {
        return new LexerImpl(options.getModule().build(), options.getListener());
    }
}
