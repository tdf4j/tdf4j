package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokenActionTest extends ParserTest {

    @Test
    public void normal() {
        final List<String> tokens = new ArrayList<>();
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "tokens", tokens)
                        );
                prod("prod1")
                        .is(
                                t("A", "tokens.add(token.getValue());"),
                                t("B"),
                                t("C", "tokens.add(token.getValue());")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(2, tokens.size());
        assertEquals("A", tokens.get(0));
        assertEquals("C", tokens.get(1));
    }

    @Test
    public void nullable() {
        final List<String> tokens = new ArrayList<>();
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "tokens", tokens)
                        );
                prod("prod1")
                        .is(
                                t("A", null),
                                t("B"),
                                t("C", null)
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(0, tokens.size());
    }

}
