package io.github.therealmone.tdf4j.parser;

import io.github.therealmone.tdf4j.commons.Dependency;
import io.github.therealmone.tdf4j.commons.utils.FirstSetCollector;
import io.github.therealmone.tdf4j.commons.utils.FollowSetCollector;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentBindTest {

    @Test
    public void normal() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                environment()
                        .packages(
                                "package1",
                                "package2",
                                "package3"
                        )
                        .dependencies(
                                dependency(FirstSetCollector.class, "firstSetCollector", new FirstSetCollector()),
                                dependency(FollowSetCollector.class, "followSetCollector")
                        );
            }
        }.build();

        //packages
        {
            final String[] packages = module.getEnvironment().packages();
            assertEquals(3, packages.length);
            assertEquals("package1", packages[0]);
            assertEquals("package2", packages[1]);
            assertEquals("package3", packages[2]);
        }

        //dependencies
        {
            final Dependency[] dependencies = module.getEnvironment().dependencies();
            assertEquals(2, dependencies.length);
            assertEquals("firstSetCollector", dependencies[0].name());
            assertEquals("FirstSetCollector", dependencies[0].clazz().getSimpleName());
            assertEquals("FirstSetCollector", dependencies[0].instance().getClass().getSimpleName());
            assertEquals("followSetCollector", dependencies[1].name());
            assertEquals("FollowSetCollector", dependencies[1].clazz().getSimpleName());
            assertEquals("FollowSetCollector", dependencies[1].instance().getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void without_args() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                environment();
            }
        }.build();
    }

    @Test(expected = IllegalStateException.class)
    public void without_dependencies() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                environment().packages("");
            }
        }.build();
    }

    @Test(expected = IllegalStateException.class)
    public void without_packages() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                environment().dependencies(dependency(FirstSetCollector.class, "class"));
            }
        }.build();
    }
}
