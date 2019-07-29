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

package io.github.therealmone.tdf4j.tdfparser.impl;

import io.github.therealmone.tdf4j.generator.LexerOptions;
import io.github.therealmone.tdf4j.generator.impl.LexerGenerator;
import io.github.therealmone.tdf4j.lexer.Lexer;
import io.github.therealmone.tdf4j.model.Grammar;
import io.github.therealmone.tdf4j.model.ast.AST;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.tdfparser.Interpreter;
import io.github.therealmone.tdf4j.tdfparser.TdfLexerModule;
import io.github.therealmone.tdf4j.tdfparser.TdfParser;
import io.github.therealmone.tdf4j.tdfparser.TdfParserModule;
import io.github.therealmone.tdf4j.utils.FirstSetCollector;
import io.github.therealmone.tdf4j.utils.FollowSetCollector;
import io.github.therealmone.tdf4j.utils.Predictor;

import javax.annotation.Nullable;

public class TdfInterpreter implements Interpreter {
    private final Lexer lexer;
    private final TdfParser parser;

    public TdfInterpreter() {
        this.lexer = new LexerGenerator(new LexerOptions.Builder().setModule(new TdfLexerModule()).build()).generate();
        final Grammar grammar = new TdfParserModule().build().getGrammar();
        this.parser = new TdfParserImpl(null, new Predictor(
                new FirstSetCollector().collect(grammar.getProductions()),
                new FollowSetCollector().collect(grammar.getProductions())
        ));
    }

    @Nullable
    @Override
    public AbstractLexerModule getLexerModule() {
        return parser.getLexerModule();
    }

    @Nullable
    @Override
    public AbstractParserModule getParserModule() {
        return parser.getParserModule();
    }

    @Override
    public AST parse(final CharSequence input) {
        return parser.parse(lexer.stream(input));
    }

}
