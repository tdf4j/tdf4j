package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.UnexpectedTokenException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnexpectedTokensTest extends ParserTest {

    @Test
    public void terminals() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                prod("prod1").is(t("A"), t("B"), t("C"));
            }
        });
        try {
            parse(parser, "ACB");
            fail("Expected UnexpectedTokenException");
        } catch (UnexpectedTokenException e) {
            assertEquals("Unexpected token: Token{tag=C, value=C, row=1, column=1}. Expected: [B]", e.getMessage());
        }
    }

    @Test
    public void or() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                prod("prod1").is(or(t("A"), t("B")));
            }
        });
        try {
            parse(parser, "C");
            fail("Expected UnexpectedTokenException");
        } catch (UnexpectedTokenException e) {
            assertEquals("Unexpected token: Token{tag=C, value=C, row=1, column=0}. Expected: [A, B]", e.getMessage());
        }
    }

    @Test
    public void or_of_non_terminals() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                prod("prod1").is(or(nt("a"), nt("b")));
                prod("a").is(t("A"));
                prod("b").is(t("B"));
            }
        });
        try {
            parse(parser, "C");
            fail("Expected UnexpectedTokenException");
        } catch (UnexpectedTokenException e) {
            assertEquals("Unexpected token: Token{tag=C, value=C, row=1, column=0}. Expected: [a, b]", e.getMessage());
        }
    }

}
