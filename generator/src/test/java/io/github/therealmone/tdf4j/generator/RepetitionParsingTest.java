package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A, 1, 2));
        assertParserFails(parser, "BBBBBBC", unexpectedToken(TestTerminal.B, 1, 0));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C, 1, 7));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B, 1, 8));
        assertParserFails(parser, "AAC", unexpectedToken(TestTerminal.C, 1, 2));
        assertParserFails(parser, "", unexpectedEOF());
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.B, 1, 3));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.C, 1, 1));
        assertParserFails(parser, "BBBC", unexpectedToken(TestTerminal.C, 1, 3));
        assertParserFails(parser, "BBBBBC", unexpectedToken(TestTerminal.C, 1, 5));
        assertParserFails(parser, "BBBBBBBC", unexpectedToken(TestTerminal.C, 1, 7));
        assertParserFails(parser, "", unexpectedEOF());
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A, 1, 2));
        assertParserFails(parser, "BBBBBBC", unexpectedToken(TestTerminal.B, 1, 0));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C, 1, 7));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B, 1,8));
        assertParserFails(parser, "AAC", unexpectedToken(TestTerminal.C, 1, 2));
        assertParserFails(parser, "", unexpectedEOF());
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B, 1, 1));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A, 1, 2));
        assertParserFails(parser, "AABC", unexpectedToken(TestTerminal.C, 1, 3));
        assertParserFails(parser, "AABBBC", unexpectedToken(TestTerminal.C, 1, 5));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C, 1, 7));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B, 1, 8));
        assertParserFails(parser, "", unexpectedEOF());
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
        assertParserFails(parser, "BB", unexpectedEOF());
        assertParserFails(parser, "BBBB", unexpectedEOF());
        assertParserFails(parser, "C", unexpectedEOF());
        assertParserFails(parser, "CC", unexpectedEOF());
        assertParserFails(parser, "BBC", unexpectedEOF());
        assertParserFails(parser, "CBB", unexpectedEOF());
        assertParserFails(parser, "A", unexpectedEOF());
        assertParserFails(parser, "BBB", unexpectedEOF());
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.C, 1, 1));
        assertParserFails(parser, "BBBC", unexpectedToken(TestTerminal.C, 1, 3));
        assertParserFails(parser, "BBBBBC", unexpectedToken(TestTerminal.C, 1, 5));
        assertParserFails(parser, "CB", unexpectedEOF());
        assertParserFails(parser, "CBBB", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }
}
