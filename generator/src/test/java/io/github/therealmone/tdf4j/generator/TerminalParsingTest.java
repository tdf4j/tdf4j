package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalParsingTest extends ParserTest {
    /**
     * prod1 := A, B, C
     */
    @Test
    public void sequence() {
        {
            final Parser parser = generate(new AbstractParserModule() {
                @Override
                public void configure() {
                    prod("prod1").is(t("A"), t("B"), t("C"));
                }
            });
            assertNotNull(parse(parser, "ABC"));
            assertParserFails(parser, "ACB", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        }

        {
            final Parser parser = generate(new AbstractParserModule() {
                @Override
                public void configure() {
                    prod("prod1").then(nt("prod2"));
                    prod("prod2").is(t("A"), nt("prod3"));
                    prod("prod3").is(t("B"), nt("prod4"));
                    prod("prod4").then(t("C"));
                }
            });
            assertNotNull(parse(parser, "ABC"));
            assertParserFails(parser, "ACB", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        }
    }

    /**
     * prod1 := A | B, C
     */
    @Test
    public void with_or() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").is(or(t("A"), t("B")), t("C"));
            }
        });
        assertNotNull(parse(parser, "AC"));
        assertNotNull(parse(parser, "BC"));
        assertParserFails(parser, "CA", unexpectedToken(TestTerminal.C, "A", "B"));
        assertParserFails(parser, "CB", unexpectedToken(TestTerminal.C, "A", "B"));
    }

    /**
     * prod1 := [A], B, [C]
     */
    @Test
    public void with_optional() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").is(optional(t("A")), t("B"), optional(t("C")));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertNotNull(parse(parser, "BC"));
        assertNotNull(parse(parser, "AB"));
        assertNotNull(parse(parser, "B"));
        assertParserFails(parser, "CB", unexpectedToken(TestTerminal.C, "B"));
        assertParserFails(parser, "ACB", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        assertParserFails(parser, "", unexpectedEOF("B"));
    }

    /**
     * prod1 := {A, B}, C
     */
    @Test
    public void with_repeat() {
        final Parser parser = generate(new AbstractParserModule() {
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
        assertParserFails(parser, "AC", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        assertParserFails(parser, "BAC", unexpectedToken(TestTerminal.B, "C"));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.B, "C"));
    }

    /**
     * prod1 := (A, B), C
     */
    @Test
    public void with_group() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").is(group(t("A"), t("B")), t("C"));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertParserFails(parser, "AC", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.B, "A"));
        assertParserFails(parser, "B", unexpectedToken(TestTerminal.B, "A"));
        assertParserFails(parser, "C", unexpectedToken(TestTerminal.C, "A"));
    }
}
