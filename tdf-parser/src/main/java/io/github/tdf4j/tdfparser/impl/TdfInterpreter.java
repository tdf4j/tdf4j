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

package io.github.tdf4j.tdfparser.impl;

import io.github.tdf4j.core.model.Grammar;
import io.github.tdf4j.core.model.ast.AST;
import io.github.tdf4j.core.module.LexerAbstractModule;
import io.github.tdf4j.core.module.ParserAbstractModule;
import io.github.tdf4j.tdfparser.Interpreter;
import io.github.tdf4j.tdfparser.TdfParser;
import io.github.tdf4j.tdfparser.TdfParserModule;

import javax.annotation.Nullable;

public class TdfInterpreter implements Interpreter {
    private final TdfParser parser;

    public TdfInterpreter() {
        final Grammar grammar = new TdfParserModule().build().getGrammar();
        this.parser = new TdfParserImpl(grammar);
    }

    @Nullable
    @Override
    public LexerAbstractModule getLexerModule() {
        return parser.getLexerModule();
    }

    @Nullable
    @Override
    public ParserAbstractModule getParserModule() {
        return parser.getParserModule();
    }

    @Override
    public AST parse(final CharSequence input) {
        return parser.parse(input);
    }

}
