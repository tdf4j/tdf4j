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
package io.github.therealmone.tdf4j.generator.impl;

import io.github.therealmone.tdf4j.generator.Generator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.lexer.SymbolListener;
import io.github.therealmone.tdf4j.lexer.impl.LexerImpl;
import io.github.therealmone.tdf4j.lexer.impl.SymbolListenerImpl;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;

public class LexerGenerator implements Generator<Lexer> {
    private final AbstractLexerModule module;
    private final SymbolListener listener;

    public LexerGenerator(final AbstractLexerModule module) {
        this.module = module;
        this.listener = new SymbolListenerImpl();
    }

    public LexerGenerator(final AbstractLexerModule module, final SymbolListener listener) {
        this.module = module;
        this.listener = listener;
    }

    @Override
    public Lexer generate() {
        return new LexerImpl(module.build(), listener);
    }
}
