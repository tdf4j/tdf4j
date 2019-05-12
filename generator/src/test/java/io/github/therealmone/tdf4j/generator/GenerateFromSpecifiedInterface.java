package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenerateFromSpecifiedInterface extends ParserTest {
    public interface TestInterface extends Parser {
        String getTestString();
    }

    @Test
    public void normal() {
        final TestInterface parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment().code(
                        "@Override\n" +
                                "public String getTestString() {\n" +
                                "   return \"TestValue\";\n" +
                                "}\n"
                );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        }, TestInterface.class);
        assertEquals("TestValue", parser.getTestString());
        assertNotNull(parse(parser, "ABC"));
    }

    @Test(expected = NullPointerException.class)
    public void null_interface() {
        generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment().code(
                        "@Override\n" +
                                "public String getTestString() {\n" +
                                "   return \"TestValue\";\n" +
                                "}\n"
                );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        }, null);
    }
}
