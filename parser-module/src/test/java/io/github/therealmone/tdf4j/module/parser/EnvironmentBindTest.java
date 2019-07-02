package io.github.therealmone.tdf4j.module.parser;

import io.github.therealmone.tdf4j.model.Dependency;
import io.github.therealmone.tdf4j.utils.FirstSetCollector;
import io.github.therealmone.tdf4j.utils.FollowSetCollector;
import org.junit.Assert;
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
            Assert.assertEquals("firstSetCollector", dependencies[0].name());
            Assert.assertEquals("FirstSetCollector", dependencies[0].clazz().getSimpleName());
            Assert.assertEquals("FirstSetCollector", dependencies[0].instance().getClass().getSimpleName());
            Assert.assertEquals("followSetCollector", dependencies[1].name());
            Assert.assertEquals("FollowSetCollector", dependencies[1].clazz().getSimpleName());
            Assert.assertEquals("FollowSetCollector", dependencies[1].instance().getClass().getSimpleName());
        }
    }

    @Test
    public void without_args() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                environment();
            }
        }.build();
        assertEquals(0, module.getEnvironment().packages().length);
        assertEquals(0, module.getEnvironment().dependencies().length);
    }

    @Test
    public void without_dependencies() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                environment().packages("");
            }
        }.build();
        assertEquals(1, module.getEnvironment().packages().length);
        assertEquals(0, module.getEnvironment().dependencies().length);
    }

    @Test
    public void without_packages() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                environment().dependencies(dependency(FirstSetCollector.class, "class"));
            }
        }.build();
        assertEquals(0, module.getEnvironment().packages().length);
        assertEquals(1, module.getEnvironment().dependencies().length);
    }
}
