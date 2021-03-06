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

import io.github.tdf4j.core.module.ParserAbstractModule;
import io.github.tdf4j.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalParsingTest extends ParserTest {
    /**
     * prod1 := A, B, C
     */
    @Test
    public void sequence() {
        {
            final Parser parser = generateParser(new ParserAbstractModule() {
                @Override
                public void configure() {
                    prod("prod1").is(t("A"), t("B"), t("C"));
                }
            });
            assertNotNull(parse(parser, "ABC"));
            assertParserFails(parser, "ACB", unexpectedToken(TestLetter.C, 1, 1, "B"));
        }

        {
            final Parser parser = generateParser(new ParserAbstractModule() {
                @Override
                public void configure() {
                    prod("prod1").then(nt("prod2"));
                    prod("prod2").is(t("A"), nt("prod3"));
                    prod("prod3").is(t("B"), nt("prod4"));
                    prod("prod4").then(t("C"));
                }
            });
            assertNotNull(parse(parser, "ABC"));
            assertParserFails(parser, "ACB", unexpectedToken(TestLetter.C, 1, 1, "B"));
        }
    }

    /**
     * prod1 := A | B, C
     */
    @Test
    public void with_or() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1").is(or(t("A"), t("B")), t("C"));
            }
        });
        assertNotNull(parse(parser, "AC"));
        assertNotNull(parse(parser, "BC"));
        assertParserFails(parser, "CA", unexpectedToken(TestLetter.C, "A", "B"));
        assertParserFails(parser, "CB", unexpectedToken(TestLetter.C, "A", "B"));
    }

    /**
     * prod1 := [A], B, [C]
     */
    @Test
    public void with_optional() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1").is(optional(t("A")), t("B"), optional(t("C")));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertNotNull(parse(parser, "BC"));
        assertNotNull(parse(parser, "AB"));
        assertNotNull(parse(parser, "B"));
        assertParserFails(parser, "CB", unexpectedToken(TestLetter.C, "B"));
        assertParserFails(parser, "ACB", unexpectedToken(TestLetter.C, 1, 1, "B"));
        assertParserFails(parser, "", unexpectedEOF("B"));
    }

    /**
     * prod1 := {A, B}, C
     */
    @Test
    public void with_repeat() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(t("A"), t("B")))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertNotNull(parse(parser, "ABABC"));
        assertNotNull(parse(parser, "ABABABC"));
        assertNotNull(parse(parser, "C"));
        assertParserFails(parser, "AC", unexpectedToken(TestLetter.C, 1, 1, "B"));
        assertParserFails(parser, "BAC", unexpectedToken(TestLetter.B, "C"));
        assertParserFails(parser, "BC", unexpectedToken(TestLetter.B, "C"));
    }

    /**
     * prod1 := (A, B), C
     */
    @Test
    public void with_group() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1").is(group(t("A"), t("B")), t("C"));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertParserFails(parser, "AC", unexpectedToken(TestLetter.C, 1, 1, "B"));
        assertParserFails(parser, "BC", unexpectedToken(TestLetter.B, "A"));
        assertParserFails(parser, "B", unexpectedToken(TestLetter.B, "A"));
        assertParserFails(parser, "C", unexpectedToken(TestLetter.C, "A"));
    }
}
