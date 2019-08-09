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

import org.tdf4j.core.module.ParserAbstractModule;
import org.tdf4j.core.utils.FirstSetCollector;
import org.tdf4j.core.utils.FollowSetCollector;
import org.tdf4j.parser.Parser;
import org.apache.commons.digester3.Digester;
import org.joor.ReflectException;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentGenerationTest extends ParserTest {

    @Test
    public void packages() {
        final Parser parser = generate(new ParserAbstractModule() {
            @Override
            public void configure() {
                environment()
                        .setPackages(
                                "java.lang.*",
                                "org.tdf4j.parser.Parser"
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
        final Parser parser = generate(new ParserAbstractModule() {
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
        generate(new ParserAbstractModule() {
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
        final Parser parser = generate(new ParserAbstractModule() {
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
