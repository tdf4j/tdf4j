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

public class GroupParsingTest extends ParserTest {

    /**
     * prod1 := ((A), ((B, C)))
     */
    @Test
    public void nested() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(group(
                                group(t("A")),
                                group(
                                        group(t("B"), t("C"))
                                )
                        ));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertParserFails(parser, "AC", unexpectedToken(TestLetter.C, 1, 1, "B"));
        assertParserFails(parser, "AB", unexpectedEOF("C"));
        assertParserFails(parser, "BC", unexpectedToken(TestLetter.B, "A"));
        assertParserFails(parser, "C", unexpectedToken(TestLetter.C, "A"));
        assertParserFails(parser, "", unexpectedEOF("A"));
    }

    /**
     * prod1 := (A, B) | (C | A)
     */
    @Test
    public void with_or() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                group(t("A"), t("B")),
                                group(or(
                                        t("C"),
                                        t("A")
                                ))
                        ));
            }
        });
        assertNotNull(parse(parser, "AB"));
        assertNotNull(parse(parser, "C"));
        assertParserFails(parser, "A", unexpectedEOF("B"));
        assertParserFails(parser, "B", unexpectedToken(TestLetter.B, "A", "C", "A"));
        assertParserFails(parser, "", unexpectedEOF("A", "C", "A"));
    }

    /**
     * prod1 := [(A, B)], ([C, C]), A
     */
    @Test
    public void with_optional() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(optional(group(t("A"), t("B"))))
                        .then(group(optional(t("C"), t("C"))))
                        .then(t("A"));
            }
        });
        assertNotNull(parse(parser, "ABCCA"));
        assertNotNull(parse(parser, "ABA"));
        assertNotNull(parse(parser, "CCA"));
        assertParserFails(parser, "A", unexpectedEOF("B"));
        assertParserFails(parser, "B", unexpectedToken(TestLetter.B, "A"));
        assertParserFails(parser, "CA", unexpectedToken(TestLetter.A, 1, 1, "C"));
        assertParserFails(parser, "CB", unexpectedToken(TestLetter.B, 1, 1, "C"));
    }

    /**
     * prod1 := {(A, B)}, ({C, A})
     */
    @Test
    public void with_repeat() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(group(t("A"), t("B"))))
                        .then(group(repeat(t("C")), t("A")));
            }
        });

        assertNotNull(parse(parser, "ABCA"));
        assertNotNull(parse(parser, "CA"));
        assertNotNull(parse(parser, "ABABCA"));
        assertNotNull(parse(parser, "ABCCA"));
        assertNotNull(parse(parser, "ABABCCA"));
        assertParserFails(parser, "ABA", unexpectedEOF("B"));
        assertParserFails(parser, "A", unexpectedEOF("B"));
        assertParserFails(parser, "B", unexpectedToken(TestLetter.B, "A"));
        assertParserFails(parser, "BC", unexpectedToken(TestLetter.B, "A"));
        assertParserFails(parser, "", unexpectedEOF("A"));
    }
}
