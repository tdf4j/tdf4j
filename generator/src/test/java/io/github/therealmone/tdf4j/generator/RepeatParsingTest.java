package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class RepeatParsingTest extends ParserTest {

    /**
     * prod1 := {{A, B}, C}
     */
    @Test
    public void nested() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(
                                repeat(t("A"), t("B")),
                                t("C")
                        ));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertNotNull(parse(parser, "ABABC"));
        assertNotNull(parse(parser, "C"));
        assertNotNull(parse(parser, "ABCABC"));
        assertNotNull(parse(parser, ""));
        assertNotNull(parse(parser, "CCC"));
    }

    /**
     * prod1 := {A, B} | {C | A}
     */
    @Test
    //todo: fix Expected: [A, C, A]
    public void with_or() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(or(
                                repeat(t("A"), t("B")),
                                repeat(
                                        or(t("C"), t("A"))
                                )
                        ));
            }
        });
        assertNotNull(parse(parser, "AB"));
        assertNotNull(parse(parser, "ABAB"));
        assertNotNull(parse(parser, "C"));
        assertNotNull(parse(parser, "CC"));
        assertParserFails(parser, "AA", unexpectedToken(TestTerminal.A, 1, 1, "B"));
        assertParserFails(parser, "A", unexpectedEOF("B"));
        assertParserFails(parser, "", unexpectedEOF("A", "C", "A"));
    }

    /**
     * prod1 := {[A, B]},[{C}, A]
     */
    @Test
    @Ignore//todo: {[...]} - разрешать? Получается бесконечный цикл
    public void with_optional() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(optional(t("A"), t("B"))))
                        .then(optional(repeat(t("C")), t("A")));
            }
        });
        assertNotNull(parse(parser, "ABCA"));
        assertNotNull(parse(parser, ""));
        assertNotNull(parse(parser, "ABABCCACCACACA"));
        assertNotNull(parse(parser, "AB"));
        assertNotNull(parse(parser, "CA"));
        assertNotNull(parse(parser, "A"));
    }

    /**
     * prod1 := {(A, B)},({C}, A)
     */
    @Test
    public void with_group() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .then(repeat(group(t("A"), t("B"))))
                        .then(group(repeat(t("C")), t("A")));
            }
        });
        assertNotNull(parse(parser, "CCA"));
        assertNotNull(parse(parser, "ABCCA"));
        assertNotNull(parse(parser, "ABCA"));
        assertParserFails(parser, "AAABCCA", unexpectedToken(TestTerminal.A, 1, 1, "B"));
        assertParserFails(parser, "A", unexpectedEOF("B"));
        assertParserFails(parser, "ABA", unexpectedEOF("B"));
        assertParserFails(parser, "BCA", unexpectedToken(TestTerminal.B, "A"));
        assertParserFails(parser, "", unexpectedEOF("A"));
    }
}
