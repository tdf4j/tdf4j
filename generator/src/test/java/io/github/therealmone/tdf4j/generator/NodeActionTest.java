package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NodeActionTest extends ParserTest {

    @Test
    public void normal() {
        final List<String> nodes = new ArrayList<>();
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "tokens", nodes)
                        );
                prod("prod1")
                        .is(
                                nt("a", "tokens.add(node.getTag());"),
                                nt("b"),
                                nt("c", "tokens.add(node.getTag());")
                        );
                prod("a").is(t("A"));
                prod("b").is(t("B"));
                prod("c").is(t("C"));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(2, nodes.size());
        assertEquals("a", nodes.get(0));
        assertEquals("c", nodes.get(1));
    }

    @Test
    public void nullable() {
        final List<String> nodes = new ArrayList<>();
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            protected void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "tokens", nodes)
                        );
                prod("prod1")
                        .is(
                                nt("a", null),
                                nt("b"),
                                nt("c", null)
                        );
                prod("a").is(t("A"));
                prod("b").is(t("B"));
                prod("c").is(t("C"));
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(0, nodes.size());
    }

}
