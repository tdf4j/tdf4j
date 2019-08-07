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

package org.tdf4j.tdfparser;

import org.tdf4j.generator.LexerOptions;
import org.tdf4j.generator.ParserOptions;
import org.tdf4j.generator.impl.LexerGenerator;
import org.tdf4j.generator.impl.ParserGenerator;
import org.tdf4j.lexer.Lexer;
import org.junit.Before;

public class TdfGrammarTest extends FullGrammarTest {

    @Override
    @Before
    public void before() {
        final Interpreter interpreter = generate("TdfGrammar.tdf");
        System.out.println(interpreter.getLexerModule().build().getTerminals());
        System.out.println(interpreter.getParserModule().build().getGrammar());

        final TdfParser tempParser = (TdfParser) new ParserGenerator(new ParserOptions.Builder()
                .setPackage("org.tdf4j.tdfparser")
                .setClassName("TdfGrammarTest_tempParser")
                .setModule(interpreter.getParserModule())
                .setInterface(TdfParser.class)
                .build()
        ).generate();
        final Lexer tempLexer = new LexerGenerator(new LexerOptions.Builder()
                .setModule(interpreter.getLexerModule())
                .build()
        ).generate();
        System.out.println(tempParser.parse(tempLexer.stream(load("FullGrammarTest.tdf"))));
        System.out.println(tempParser.getLexerModule().build().getTerminals());
        System.out.println(tempParser.getParserModule().build().getGrammar());

        this.parser = new ParserGenerator(new ParserOptions.Builder()
                .setPackage("org.tdf4j.tdfparser")
                .setClassName("TdfGrammarTest_parser")
                .setModule(tempParser.getParserModule())
                .build()
        ).generate();
        this.lexer = new LexerGenerator(new LexerOptions.Builder()
                .setModule(tempParser.getLexerModule())
                .build()
        ).generate();
    }
}
