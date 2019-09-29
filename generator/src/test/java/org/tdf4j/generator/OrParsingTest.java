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

package org.tdf4j.generator;

import org.tdf4j.core.module.ParserAbstractModule;
import org.tdf4j.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrParsingTest extends ParserTest {

    /**
     * prod1 := A | B | C
     */
    @Test
    public void nested() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                t("A"),
                                or(t("B"), t("C"))
                        ));
            }
        });
        assertNotNull(parse(parser, "A"));
        assertNotNull(parse(parser, "B"));
        assertNotNull(parse(parser, "C"));
        assertParserFails(parser, "", unexpectedEOF("A", "B", "C"));
    }

    /**
     * prod1 := [A | B], C
     */
    @Test
    public void with_optional() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(optional(or(t("A"), t("B"))))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "AC"));
        assertNotNull(parse(parser, "BC"));
        assertNotNull(parse(parser, "C"));
        assertParserFails(parser, "ABC", unexpectedToken(TestLetter.B, 1, 1, "C"));
        assertParserFails(parser, "AB", unexpectedToken(TestLetter.B, 1, 1, "C"));
        assertParserFails(parser, "BAC", unexpectedToken(TestLetter.A, 1, 1, "C"));
        assertParserFails(parser, "BA", unexpectedToken(TestLetter.A, 1, 1, "C"));
        assertParserFails(parser, "", unexpectedEOF("C"));
    }

    /**
     * prod1 := {A | B}, C
     */
    @Test
    public void with_repeat() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(or(t("A"), t("B"))))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertNotNull(parse(parser, "BC"));
        assertNotNull(parse(parser, "AC"));
        assertNotNull(parse(parser, "AAAC"));
        assertNotNull(parse(parser, "BBBC"));
        assertNotNull(parse(parser, "AABBC"));
        assertNotNull(parse(parser, "C"));
        assertParserFails(parser, "", unexpectedEOF("C"));
    }

    /**
     * prod1 := (A, B) | C
     */
    @Test
    public void with_group() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                group(t("A"), t("B")),
                                t("C")
                        ));
            }
        });
        assertNotNull(parse(parser, "AB"));
        assertNotNull(parse(parser, "C"));
        assertParserFails(parser, "AC", unexpectedToken(TestLetter.C, 1, 1, "B"));
        assertParserFails(parser, "B", unexpectedToken(TestLetter.B, "A", "C"));
        assertParserFails(parser, "", unexpectedEOF("A", "C"));
    }
}
