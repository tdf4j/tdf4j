package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A));
        assertParserFails(parser, "BBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AAC", unexpectedToken(TestTerminal.C));
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "BBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "BBBBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "BBBBBBBC", unexpectedToken(TestTerminal.C));
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A));
        assertParserFails(parser, "BBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AAC", unexpectedToken(TestTerminal.C));
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
        assertParserFails(parser, "ABBBBBBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AAABBBBBBC", unexpectedToken(TestTerminal.A));
        assertParserFails(parser, "AABC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "AABBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "AABBBBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "AABBBBBBBC", unexpectedToken(TestTerminal.B));
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
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "BBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "BBBBBC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "CB", unexpectedEOF());
        assertParserFails(parser, "CBBB", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }
}
