package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrParsingTest extends ParserTest {

    /**
     * prod1 := A | B | C
     */
    @Test
    public void nested() {
        final Parser parser = generate(new AbstractParserModule() {
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
        final Parser parser = generate(new AbstractParserModule() {
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
        assertParserFails(parser, "ABC", unexpectedToken(TestTerminal.B, 1, 1, "C"));
        assertParserFails(parser, "AB", unexpectedToken(TestTerminal.B, 1, 1, "C"));
        assertParserFails(parser, "BAC", unexpectedToken(TestTerminal.A, 1, 1, "C"));
        assertParserFails(parser, "BA", unexpectedToken(TestTerminal.A, 1, 1, "C"));
        assertParserFails(parser, "", unexpectedEOF("C"));
    }

    /**
     * prod1 := {A | B}, C
     */
    @Test
    public void with_repeat() {
        final Parser parser = generate(new AbstractParserModule() {
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
        final Parser parser = generate(new AbstractParserModule() {
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
        assertParserFails(parser, "AC", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        assertParserFails(parser, "B", unexpectedToken(TestTerminal.B, "A", "C"));
        assertParserFails(parser, "", unexpectedEOF("A", "C"));
    }
}
