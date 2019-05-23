package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AdditionalCodeGenerationTest extends ParserTest {

    @Test
    public void with_inline_actions() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .code(
                                "private void sayHello() {" +
                                    "System.out.println(\"Hello\");" +
                                "}"
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C"),
                                inline("sayHello();")
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
                        .code(
                                "private void test() {" +
                                        "list.add(\"testvalue\");" +
                                "}"
                        )
                        .dependencies(
                                dependency(List.class, "list", test)
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C"),
                                inline("test();")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals("testvalue", test.get(0));
    }
}
