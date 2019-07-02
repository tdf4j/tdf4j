package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.utils.FirstSetCollector;
import io.github.therealmone.tdf4j.utils.FollowSetCollector;
import io.github.therealmone.tdf4j.parser.Parser;
import org.apache.commons.digester3.Digester;
import org.joor.ReflectException;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentGenerationTest extends ParserTest {

    @Test
    public void packages() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .setPackages(
                                "java.lang.*",
                                "io.github.therealmone.tdf4j.parser.Parser"
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
    }

    @Test
    public void dependencies() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(FirstSetCollector.class, "firstSetCollector", new FirstSetCollector()),
                                dependency(FollowSetCollector.class, "followSetCollector")
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
    }

    @Test(expected = ReflectException.class)
    public void unknown_classes() {
        generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(Digester.class, "digester")
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        });
    }

    @Test
    public void specified_package() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .setPackages(
                                "org.apache.commons.digester3.Digester"
                        )
                        .setDependencies(
                                dependency(Digester.class, "digester")
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
    }
}
