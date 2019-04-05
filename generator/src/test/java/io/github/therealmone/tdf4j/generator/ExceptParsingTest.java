package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore("Unsupported")
public class ExceptParsingTest extends ParserTest {

    /**
     * prod1 := A,-(A,C),C
     */
    @Test
    public void except() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(t("A"))
                        .then(except(t("A"), t("C")))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "AC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "ACC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "AAC", unexpectedToken(TestTerminal.A));
        assertParserFails(parser, "C", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "A", unexpectedEOF());
        assertParserFails(parser, "AB", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }

    /**
     * prod1 := (-(A),-(B)),-(C)
     */
    @Test
    public void with_group() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(group(except(t("A")), except(t("B"))))
                        .then(except(t("C")));
            }
        });
        assertNotNull(parse(parser, "BAB"));
        assertNotNull(parse(parser, "BAA"));
        assertNotNull(parse(parser, "BCB"));
        assertNotNull(parse(parser, "BCA"));
        assertNotNull(parse(parser, "CAB"));
        assertNotNull(parse(parser, "CAA"));
        assertNotNull(parse(parser, "CCB"));
        assertNotNull(parse(parser, "CCA"));
        assertParserFails(parser, "ABC", unexpectedToken(TestTerminal.A));
        assertParserFails(parser, "BBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "BAC", unexpectedToken(TestTerminal.C));
        assertParserFails(parser, "B", unexpectedEOF());
        assertParserFails(parser, "BA", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }

    /**
     * prod1 := [-(A)],[-(B)],C
     */
    @Test
    public void with_optional() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(optional(except(t("A"))))
                        .then(optional(except(t("B"))))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "BAC"));
        assertNotNull(parse(parser, "CAC"));
        assertNotNull(parse(parser, "BCC"));
        assertNotNull(parse(parser, "CCC"));
        assertParserFails(parser, "ABC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "CBC", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "BC", unexpectedEOF());
        assertParserFails(parser, "C", unexpectedEOF());
        assertParserFails(parser, "CA", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }

    /**
     * prod1 := -(A,B)|-(C),C
     */
    @Test
    public void with_or() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                except(t("A"), t("B")),
                                except(t("C"))
                        ))
                        .then(t("C"));
            }
        });
        assertNotNull(parse(parser, "AC"));
        assertNotNull(parse(parser, "BC"));
        assertNotNull(parse(parser, "CC"));
        assertParserFails(parser, "CB", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "CA", unexpectedToken(TestTerminal.A));
        assertParserFails(parser, "AB", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "BB", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "C", unexpectedEOF());
        assertParserFails(parser, "A", unexpectedEOF());
        assertParserFails(parser, "B", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }

    /**
     * prod1 := {-(C)},C
     */
    @Test
    public void with_repeat() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                repeat(except(t("C"))),
                                t("C")
                        );
            }
        });
        assertNotNull(parse(parser, "C"));
        assertNotNull(parse(parser, "ABABABC"));
        assertNotNull(parse(parser, "AC"));
        assertNotNull(parse(parser, "BC"));
        assertNotNull(parse(parser, "AAC"));
        assertNotNull(parse(parser, "BBC"));
        assertNotNull(parse(parser, "ABC"));
        assertNotNull(parse(parser, "BAC"));
        assertParserFails(parser, "A", unexpectedEOF());
        assertParserFails(parser, "B", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }

    /**
     * prod1 := 2*-(C),3*2*-(A,B),C
     */
    @Test
    public void with_repetition() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                repetition(except(t("C")), 2),
                                repetition(
                                        repetition(except(t("A"), t("B")), 2),
                                        3
                                ),
                                t("C")
                        );
            }
        });
        assertNotNull(parse(parser, "ABCCCCCCC"));
        assertNotNull(parse(parser, "AACCCCCCC"));
        assertNotNull(parse(parser, "BACCCCCCC"));
        assertNotNull(parse(parser, "BBCCCCCCC"));
        assertParserFails(parser, "ABCCCCCCA", unexpectedToken(TestTerminal.A));
        assertParserFails(parser, "ABCCCCCCB", unexpectedToken(TestTerminal.B));
        assertParserFails(parser, "ABCCCCCC", unexpectedEOF());
        assertParserFails(parser, "AB", unexpectedEOF());
        assertParserFails(parser, "", unexpectedEOF());
    }
}
