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

import org.tdf4j.module.parser.AbstractParserModule;
import org.tdf4j.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class RepetitionParsingTest extends ParserTest {

    /**
     * prod1 := 2*A,2*3*B,C
     */
    @Test
    public void nested() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repetition(t("A"), 2))
                        .then(repetition(
                                repetition(t("B"), 3),
                                2
                        ))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "AABBBBBBC"));
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1, "A"));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A, 1, 2, "B"));
        assertParserFails(parser, "BBBBBBC", unexpectedToken(TestTerminal.B, 1, 0, "A"));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C, 1, 7, "B"));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B, 1, 8, "C"));
        assertParserFails(parser, "AAC", unexpectedToken(TestTerminal.C, 1, 2, "B"));
        assertParserFails(parser, "", unexpectedEOF("A"));
    }

    /**
     * prod1 := {2*A},3*{2*B},C
     */
    @Test
    public void with_repeat() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(repetition(t("A"), 2)))
                        .then(repetition(
                                repeat(repetition(t("B"), 2)),
                                3
                        ))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "AABBBBBBC"));
        assertNotNull(parse(parser, "BBBBBBC"));
        assertNotNull(parse(parser, "AAC"));
        assertNotNull(parse(parser, "AABBC"));
        assertNotNull(parse(parser, "AABBBBC"));
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1, "A"));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.B, 1, 3, "A"));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        assertParserFails(parser, "BBBC", unexpectedToken(TestTerminal.C, 1, 3, "B"));
        assertParserFails(parser, "BBBBBC", unexpectedToken(TestTerminal.C, 1, 5, "B"));
        assertParserFails(parser, "BBBBBBBC", unexpectedToken(TestTerminal.C, 1, 7, "B"));
        assertParserFails(parser, "", unexpectedEOF("C"));
    }

    /**
     * prod1 := (2*A,3*(2*B)),C
     */
    @Test
    public void with_group() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(group(
                                repetition(t("A"), 2),
                                repetition(
                                        group(repetition(t("B"), 2)),
                                        3
                                )
                        ))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "AABBBBBBC"));
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1, "A"));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A, 1, 2, "B"));
        assertParserFails(parser, "BBBBBBC", unexpectedToken(TestTerminal.B, 1, 0, "A"));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C, 1, 7, "B"));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B, 1,8, "C"));
        assertParserFails(parser, "AAC", unexpectedToken(TestTerminal.C, 1, 2, "B"));
        assertParserFails(parser, "", unexpectedEOF("A"));
    }

    /**
     * prod1 := [2*A],3*[2*B],C
     */
    @Test
    public void with_optional() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(optional(repetition(t("A"), 2)))
                        .then(repetition(
                                optional(repetition(t("B"), 2)),
                                3
                        ))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "AABBBBBBC"));
        assertNotNull(parse(parser, "BBBBBBC"));
        assertNotNull(parse(parser, "C"));
        assertNotNull(parse(parser, "AAC"));
        assertNotNull(parse(parser, "BBC"));
        assertNotNull(parse(parser, "BBBBC"));
        assertNotNull(parse(parser, "BBBBBBC"));
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1, "A"));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A, 1, 2, "C"));
        assertParserFails(parser, "AABC", unexpectedToken(TestTerminal.C, 1, 3, "B"));
        assertParserFails(parser, "AABBBC", unexpectedToken(TestTerminal.C, 1, 5, "B"));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C, 1, 7, "B"));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B, 1, 8, "C"));
        assertParserFails(parser, "", unexpectedEOF("C"));
    }

    /**
     * prod1 := 2*A|3*(2*B|C)
     */
    @Test
    public void with_or() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                repetition(t("A"), 2),
                                repetition(
                                        group(or(
                                                repetition(t("B"), 2),
                                                t("C")
                                        )),
                                        3
                                )
                        ));

            }
        });
        assertNotNull(parse(parser, "AA"));
        assertNotNull(parse(parser, "BBBBBB"));
        assertNotNull(parse(parser, "CCC"));
        assertNotNull(parse(parser, "BBCC"));
        assertNotNull(parse(parser, "BBBBC"));
        assertNotNull(parse(parser, "CBBBB"));
        assertNotNull(parse(parser, "CBBBBBB"));
        assertParserFails(parser, "BB", unexpectedEOF("B, C"));
        assertParserFails(parser, "BBBB", unexpectedEOF("B", "C"));
        assertParserFails(parser, "C", unexpectedEOF("B", "C"));
        assertParserFails(parser, "CC", unexpectedEOF("B", "C"));
        assertParserFails(parser, "BBC", unexpectedEOF("B", "C"));
        assertParserFails(parser, "CBB", unexpectedEOF("B", "C"));
        assertParserFails(parser, "A", unexpectedEOF("A"));
        assertParserFails(parser, "BBB", unexpectedEOF("B"));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        assertParserFails(parser, "BBBC", unexpectedToken(TestTerminal.C, 1, 3, "B"));
        assertParserFails(parser, "BBBBBC", unexpectedToken(TestTerminal.C, 1, 5, "B"));
        assertParserFails(parser, "CB", unexpectedEOF("B"));
        assertParserFails(parser, "CBBB", unexpectedEOF("B"));
        assertParserFails(parser, "", unexpectedEOF("A", "B", "C"));
    }
}
