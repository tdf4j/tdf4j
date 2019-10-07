/*
 * Copyright (c) 2019 tdf4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.tdf4j.core.module;

import io.github.tdf4j.core.model.Dependency;
import io.github.tdf4j.core.utils.FirstSetCollector;
import io.github.tdf4j.core.utils.FollowSetCollector;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentBindTest {

    @Test
    public void normal() {
        final ParserAbstractModule module = new ParserAbstractModule() {
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
        final ParserAbstractModule module = new ParserAbstractModule() {
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
        final ParserAbstractModule module = new ParserAbstractModule() {
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
        final ParserAbstractModule module = new ParserAbstractModule() {
            @Override
            public void configure() {
                environment().setDependencies(dependency(FirstSetCollector.class, "class"));
            }
        }.build();
        assertEquals(0, module.getEnvironment().getPackages().length);
        assertEquals(1, module.getEnvironment().getDependencies().length);
    }
}
