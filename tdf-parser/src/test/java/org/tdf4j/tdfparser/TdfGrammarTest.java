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

import org.tdf4j.generator.Options;
import org.tdf4j.generator.impl.ParserGenerator;
import org.junit.Before;

public class TdfGrammarTest extends FullGrammarTest {

    @Override
    @Before
    public void before() {
        final Interpreter interpreter = generate("TdfGrammar.tdf");
        System.out.println(interpreter.getLexerModule().build().getAlphabet());
        System.out.println(interpreter.getParserModule().build().getGrammar());

        final TdfParser tempParser = (TdfParser) new ParserGenerator(new Options.Builder()
                .setPackage("org.tdf4j.tdfparser")
                .setClassName("TdfGrammarTest_tempParser")
                .setParserModule(interpreter.getParserModule())
                .setLexerModule(interpreter.getLexerModule())
                .setInterface(TdfParser.class)
                .build()
        ).generate();
        System.out.println(tempParser.parse(load("FullGrammarTest.tdf")));
        System.out.println(tempParser.getLexerModule().build().getAlphabet());
        System.out.println(tempParser.getParserModule().build().getGrammar());

        this.parser = new ParserGenerator(new Options.Builder()
                .setPackage("org.tdf4j.tdfparser")
                .setClassName("TdfGrammarTest_parser")
                .setParserModule(tempParser.getParserModule())
                .setLexerModule(tempParser.getLexerModule())
                .build()
        ).generate();
    }
}
