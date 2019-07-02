package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class InlineActionGenerationTest extends ParserTest {

    @Test
    public void normal() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C"),
                                inline("System.out.println(\"inline action\");")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
    }

    @Test
    public void with_dependencies() {
        final List<String> test = new ArrayList<>();
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "list", test),
                                dependency(HashMap.class, "hashmap")
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C"),
                                inline(
                                        "list.add(\"testvalue1\");" +
                                                "hashmap.put(\"key\", \"testvalue2\");" +
                                                "list.add(hashmap.get(\"key\"));" +
                                                "System.out.println(\"inline action done\");"
                                )
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(2, test.size());
        assertEquals("testvalue1", test.get(0));
        assertEquals("testvalue2", test.get(1));
    }

    @Test
    public void in_middle() {
        final List<String> test = new ArrayList<>();
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "list", test),
                                dependency(HashMap.class, "hashmap")
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                inline(
                                        "assert ast.onCursor().asRoot().getChildren().size() == 2;\n" +
                                                "list.add(ast.onCursor().asRoot().getChildren().get(0).asLeaf().getToken().getValue());\n" +
                                                "hashmap.put(\"key\", ast.onCursor().asRoot().getChildren().get(1).asLeaf().getToken().getValue());\n" +
                                                "list.add(hashmap.get(\"key\"));\n" +
                                                "System.out.println(\"inline action done\");"
                                ),
                                t("C")

                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(2, test.size());
        assertEquals("A", test.get(0));
        assertEquals("B", test.get(1));
    }

    @Test
    public void at_the_beginning() {
        final List<String> test = new ArrayList<>();
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "list", test),
                                dependency(HashMap.class, "hashmap")
                        );
                prod("prod1")
                        .is(
                                inline("list.add(\"value1\");"),
                                oneOf(
                                        t("A"),
                                        t("B"),
                                        t("C")
                                )

                        );
            }
        });
        assertNotNull(parse(parser, "C"));
        assertEquals(1, test.size());
        assertEquals("value1", test.get(0));
    }
}
