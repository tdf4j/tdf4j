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

package org.tdf4j.tdfparser.impl;

import org.tdf4j.generator.LexerOptions;
import org.tdf4j.generator.impl.LexerGenerator;
import org.tdf4j.lexer.Lexer;
import org.tdf4j.model.Grammar;
import org.tdf4j.model.ast.AST;
import org.tdf4j.module.lexer.AbstractLexerModule;
import org.tdf4j.module.parser.AbstractParserModule;
import org.tdf4j.tdfparser.Interpreter;
import org.tdf4j.tdfparser.TdfLexerModule;
import org.tdf4j.tdfparser.TdfParser;
import org.tdf4j.tdfparser.TdfParserModule;
import org.tdf4j.utils.FirstSetCollector;
import org.tdf4j.utils.FollowSetCollector;
import org.tdf4j.utils.Predictor;

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
