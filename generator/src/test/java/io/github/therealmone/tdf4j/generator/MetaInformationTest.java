package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.utils.FirstSetCollector;
import io.github.therealmone.tdf4j.utils.FollowSetCollector;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class MetaInformationTest extends ParserTest {

    @Test
    public void package_test() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").is(t("A"));
            }
        });
        assertEquals("io.github.therealmone.tdf4j.generator", parser.meta().pack());
    }

    @Test
    public void imports() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").is(t("A"));
            }
        });
        final String[] imports = parser.meta().imports();
        assertEquals(8, imports.length);
        for (int i = 0; i < Imports.values().length; i++) {
            assertEquals(Imports.values()[i].getValue(), imports[i]);
        }
        assertEquals(Parser.class.getCanonicalName(), imports[imports.length - 1]);
    }

    @Test
    public void env_imports() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .packages(
                                "java.lang.*",
                                "java.util.*"
                        );
                prod("prod1").is(t("A"));
            }
        });
        final String[] envImports = parser.meta().envImports();
        assertEquals(2, envImports.length);
        assertEquals("java.lang.*", envImports[0]);
        assertEquals("java.util.*", envImports[1]);
    }

    @Test
    public void dependencies() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .dependencies(
                                dependency(FirstSetCollector.class, "firstSetCollector", new FirstSetCollector()),
                                dependency(FollowSetCollector.class, "followSetCollector")
                        );
                prod("prod1").is(t("A"));
            }
        });
        final String[] dependencies = parser.meta().dependencies();
        assertEquals(2, dependencies.length);
        assertEquals(FirstSetCollector.class.getCanonicalName(), dependencies[0]);
        assertEquals(FollowSetCollector.class.getCanonicalName(), dependencies[1]);
    }

    @Test
    public void class_name() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").is(t("A"));
            }
        });
        assertEquals("AutoGeneratedParserFrom_MetaInformationTest$5", parser.meta().className());
    }

    @Test
    @Ignore("Придется постоянно фиксить этот тест")
    public void source_code() {

    }
}
