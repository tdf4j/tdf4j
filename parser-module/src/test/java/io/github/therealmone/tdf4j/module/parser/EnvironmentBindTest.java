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
                        .setPackages(
                                "package1",
                                "package2",
                                "package3"
                        )
                        .setDependencies(
                                dependency(FirstSetCollector.class, "firstSetCollector", new FirstSetCollector()),
                                dependency(FollowSetCollector.class, "followSetCollector")
                        );
            }
        }.build();

        //packages
        {
            final String[] packages = module.getEnvironment().getPackages();
            assertEquals(3, packages.length);
            assertEquals("package1", packages[0]);
            assertEquals("package2", packages[1]);
            assertEquals("package3", packages[2]);
        }

        //dependencies
        {
            final Dependency[] dependencies = module.getEnvironment().getDependencies();
            assertEquals(2, dependencies.length);
            Assert.assertEquals("firstSetCollector", dependencies[0].getName());
            Assert.assertEquals("FirstSetCollector", dependencies[0].getClazz().getSimpleName());
            Assert.assertEquals("FirstSetCollector", dependencies[0].instance().getClass().getSimpleName());
            Assert.assertEquals("followSetCollector", dependencies[1].getName());
            Assert.assertEquals("FollowSetCollector", dependencies[1].getClazz().getSimpleName());
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
        assertEquals(0, module.getEnvironment().getPackages().length);
        assertEquals(0, module.getEnvironment().getDependencies().length);
    }

    @Test
    public void without_dependencies() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                environment().setPackages("");
            }
        }.build();
        assertEquals(1, module.getEnvironment().getPackages().length);
        assertEquals(0, module.getEnvironment().getDependencies().length);
    }

    @Test
    public void without_packages() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                environment().setDependencies(dependency(FirstSetCollector.class, "class"));
            }
        }.build();
        assertEquals(0, module.getEnvironment().getPackages().length);
        assertEquals(1, module.getEnvironment().getDependencies().length);
    }
}
