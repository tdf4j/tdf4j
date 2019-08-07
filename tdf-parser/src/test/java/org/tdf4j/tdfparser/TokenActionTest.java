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

package org.tdf4j.tdfparser;

import org.tdf4j.generator.LexerOptions;
import org.tdf4j.generator.ParserOptions;
import org.tdf4j.generator.impl.LexerGenerator;
import org.tdf4j.generator.impl.ParserGenerator;
import org.tdf4j.lexer.Lexer;
import org.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokenActionTest extends TdfParserTest {
    public static List<String> tokens = new ArrayList<>();

    @Test
    public void test() {
        final Interpreter interpreter = generate("TokenActionTest.tdf");
        final Lexer lexer = new LexerGenerator(new LexerOptions.Builder()
                .setModule(interpreter.getLexerModule())
                .build()
        ).generate();
        final Parser parser = new ParserGenerator(new ParserOptions.Builder()
                .setPackage("org.tdf4j.tdfparser")
                .setClassName("TokenActionTest_parser")
                .setModule(interpreter.getParserModule())
                .build()
        ).generate();
        assertNotNull(parser.parse(lexer.stream("ABC")));
        assertEquals(2, tokens.size());
        assertEquals("A", tokens.get(0));
        assertEquals("C", tokens.get(1));
    }

}
