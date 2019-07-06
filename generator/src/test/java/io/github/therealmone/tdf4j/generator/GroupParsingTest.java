package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class GroupParsingTest extends ParserTest {

    /**
     * prod1 := ((A), ((B, C)))
     */
    @Test
    public void nested() {
        final Parser parser = generate(new AbstractParserModule() {
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
        assertParserFails(parser, "AC", unexpectedToken(TestTerminal.C, 1, 1, "B"));
        assertParserFails(parser, "AB", unexpectedEOF("C"));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.B, "A"));
        assertParserFails(parser, "C", unexpectedToken(TestTerminal.C, "A"));
        assertParserFails(parser, "", unexpectedEOF("A"));
    }

    /**
     * prod1 := (A, B) | (C | A)
     */
    @Test
    //todo: fix Expected: [A, C, A]
    public void with_or() {
        final Parser parser = generate(new AbstractParserModule() {
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
        assertParserFails(parser, "B", unexpectedToken(TestTerminal.B, "A", "C", "A"));
        assertParserFails(parser, "", unexpectedEOF("A", "C", "A"));
    }

    /**
     * prod1 := [(A, B)], ([C, C]), A
     */
    @Test
    public void with_optional() {
        final Parser parser = generate(new AbstractParserModule() {
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
        assertParserFails(parser, "B", unexpectedToken(TestTerminal.B, "A"));
        assertParserFails(parser, "CA", unexpectedToken(TestTerminal.A, 1, 1, "C"));
        assertParserFails(parser, "CB", unexpectedToken(TestTerminal.B, 1, 1, "C"));
    }

    /**
     * prod1 := {(A, B)}, ({C, A})
     */
    @Test
    public void with_repeat() {
        final Parser parser = generate(new AbstractParserModule() {
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
        assertParserFails(parser, "B", unexpectedToken(TestTerminal.B, "A"));
        assertParserFails(parser, "BC", unexpectedToken(TestTerminal.B, "A"));
        assertParserFails(parser, "", unexpectedEOF("A"));
    }
}
