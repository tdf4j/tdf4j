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

package org.tdf4j.generator;

import org.tdf4j.generator.impl.ParserGenerator;
import org.tdf4j.module.parser.AbstractParserModule;
import org.tdf4j.utils.FirstSetCollector;
import org.tdf4j.utils.FollowSetCollector;
import org.tdf4j.parser.Parser;
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
        assertEquals("org.tdf4j.generator", parser.meta().getPackage());
    }

    @Test
    public void imports() {
        final Parser parser = generate(new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1").is(t("A"));
            }
        });
        final String[] imports = parser.meta().getImports();
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
                        .setPackages(
                                "java.lang.*",
                                "java.util.*"
                        );
                prod("prod1").is(t("A"));
            }
        });
        final String[] envImports = parser.meta().getEnvironmentImports();
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
                        .setDependencies(
                                dependency(FirstSetCollector.class, "firstSetCollector", new FirstSetCollector()),
                                dependency(FollowSetCollector.class, "followSetCollector")
                        );
                prod("prod1").is(t("A"));
            }
        });
        final String[] dependencies = parser.meta().getDependencies();
        assertEquals(2, dependencies.length);
        assertEquals(FirstSetCollector.class.getCanonicalName(), dependencies[0]);
        assertEquals(FollowSetCollector.class.getCanonicalName(), dependencies[1]);
    }

    @Test
    public void class_name() {
        final Parser parser = new ParserGenerator(new ParserOptions.Builder()
                .setPackage("org.tdf4j.generator")
                .setClassName("MetaInfTestParser")
                .setModule(new AbstractParserModule() {
                    @Override
                    public void configure() {
                        prod("prod1").is(t("A"));
                    }
                }).build()).generate();
        assertEquals("MetaInfTestParser", parser.meta().getClassName());
    }

    @Test
    @Ignore("Придется постоянно фиксить этот тест")
    public void source_code() {

    }
}
