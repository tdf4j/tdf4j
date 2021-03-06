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

package io.github.tdf4j.generator;

import io.github.tdf4j.generator.impl.ParserGenerator;
import io.github.tdf4j.core.module.LexerAbstractModule;
import io.github.tdf4j.core.module.ParserAbstractModule;
import io.github.tdf4j.parser.Parser;
import io.github.tdf4j.parser.ParserMetaInformation;
import io.github.tdf4j.parser.UnexpectedTokenException;
import io.github.tdf4j.core.model.ast.AST;
import org.junit.BeforeClass;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserTest {
    static LexerAbstractModule lexerModule;

    @BeforeClass
    public static void globalSetup() {
        lexerModule = new LexerAbstractModule() {
            @Override
            public void configure() {
                for (final TestLetter testLexeme : TestLetter.values()) {
                    tokenize(testLexeme.getLetter().getTag().getValue())
                            .pattern(testLexeme.getLetter().getPattern().pattern())
                            .priority(testLexeme.getLetter().priority());
                }
                tokenize("ws").pattern("\\s|\\n|\\r").priority(Integer.MAX_VALUE).hidden(true);
            }
        };
    }

    static Parser generateParser(final ParserAbstractModule module) {
        return generateParser(module, Parser.class);
    }

    static <T> T generateParser(final ParserAbstractModule module, final Class<? extends Parser> interfaceToImplement) {
        final long current = System.currentTimeMillis();
        final Parser parser = generate(module, interfaceToImplement).compile();
        System.out.println("Compilation time: " + (System.currentTimeMillis() - current));
        //noinspection unchecked
        return (T) parser;
    }

    static ParserMetaInformation generate(final ParserAbstractModule module) {
        return generate(module, Parser.class);
    }

    static ParserMetaInformation generate(final ParserAbstractModule module, final Class<? extends Parser> interfaceToImplement) {
        final ParserMetaInformation parser = new ParserGenerator(new Options.Builder()
                .setPackage("io.github.tdf4j.generator")
                .setClassName("ParserImpl_" + UUID.randomUUID().toString().replaceAll("-", ""))
                .setParserModule(module)
                .setLexerModule(lexerModule)
                .setInterface(interfaceToImplement)
                .build()
        ).generate();
        System.out.println(parser.getSourceCode());
        System.out.println(module.getGrammar().toString());
        System.out.println(module.getGrammar().getFirstSet().toString());
        System.out.println(module.getGrammar().getFollowSet().toString());
        return parser;
    }

    static Parser generate(final Options options) {
        final long current = System.currentTimeMillis();
        final ParserMetaInformation parser = new ParserGenerator(options).generate();
        System.out.println(parser.getSourceCode());
        System.out.println(options.getParserModule().getGrammar().toString());
        System.out.println(options.getParserModule().getGrammar().getFirstSet().toString());
        System.out.println(options.getParserModule().getGrammar().getFollowSet().toString());
        System.out.println("Compilation time: " + (System.currentTimeMillis() - current));
        return parser.compile();
    }

    static AST parse(final Parser parser, final String input) {
        final long current = System.currentTimeMillis();
        final AST ast = parser.parse(input);
        System.out.println("Parsing time: " + (System.currentTimeMillis() - current));
        System.out.println(ast);
        return ast;
    }

    static String unexpectedToken(final TestLetter testLetter) {
        return unexpectedToken(testLetter, 1, 0);
    }

    static String unexpectedToken(final TestLetter testLetter, final String... expected) {
        return unexpectedToken(testLetter, 1, 0, expected);
    }

    static String unexpectedToken(final TestLetter testLetter, final long row, final long columt) {
        return String.format("Unexpected token: Token{tag=%1$s, value=%1$s, row=%2$d, column=%3$d}",
                testLetter.getLetter().getTag().getValue(),
                row, columt);
    }

    static String unexpectedToken(final TestLetter testLetter, final long row, final long columt, final String... expected) {
        return String.format("Unexpected token: Token{tag=%1$s, value=%1$s, row=%2$d, column=%3$d}. Expected: " + Arrays.asList(expected),
                testLetter.getLetter().getTag().getValue(),
                row, columt);
    }

    static String unexpectedEOF() {
        return "Unexpected end of file";
    }

    static String unexpectedEOF(final String... expected) {
        return "Unexpected end of file. Expected: " + Arrays.asList(expected);
    }

    static void assertParserFails(final Parser parser, final String input, final String message) {
        assertThrows(() -> parse(parser, input), UnexpectedTokenException.class, message);
    }

    static void assertThrows(final Callback callback, final Class<? extends Throwable> ex, final String message) {
        try {
            callback.call();
            fail("Expected exception: " + ex.getName() + ": " + message);
        } catch (Exception e) {
            assertEquals(ex.getName(), e.getClass().getName());
            assertEquals(message, e.getMessage());
        }
    }

    @FunctionalInterface
    interface Callback {
        void call();
    }
}
