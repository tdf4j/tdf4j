/*
 * Copyright (c) 2019 Roman Fatnev
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

import org.tdf4j.module.parser.AbstractParserModule;
import org.tdf4j.parser.Parser;
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
                        .setCode(
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
                        .setCode(
                                "private void test() {" +
                                        "list.add(\"testvalue\");" +
                                "}"
                        )
                        .setDependencies(
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
