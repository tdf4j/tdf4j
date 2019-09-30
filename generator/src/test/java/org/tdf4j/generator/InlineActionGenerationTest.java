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
import org.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class InlineActionGenerationTest extends ParserTest {

    @Test
    public void normal() {
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C"),
                                inline("System.out.println(\"inline action\");")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
    }

    @Test
    public void with_dependencies() {
        final List<String> test = new ArrayList<>();
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "list", test),
                                dependency(HashMap.class, "hashmap")
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C"),
                                inline(
                                        "list.add(\"testvalue1\");" +
                                                "hashmap.put(\"key\", \"testvalue2\");" +
                                                "list.add(hashmap.get(\"key\"));" +
                                                "System.out.println(\"inline action done\");"
                                )
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(2, test.size());
        assertEquals("testvalue1", test.get(0));
        assertEquals("testvalue2", test.get(1));
    }

    @Test
    public void in_middle() {
        final List<String> test = new ArrayList<>();
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "list", test),
                                dependency(HashMap.class, "hashmap")
                        );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                inline(
                                        "assert ast.onCursor().asRoot().getChildren().size() == 2;\n" +
                                                "list.add(ast.onCursor().asRoot().getChildren().get(0).asLeaf().getToken().getValue());\n" +
                                                "hashmap.put(\"key\", ast.onCursor().asRoot().getChildren().get(1).asLeaf().getToken().getValue());\n" +
                                                "list.add(hashmap.get(\"key\"));\n" +
                                                "System.out.println(\"inline action done\");"
                                ),
                                t("C")

                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(2, test.size());
        assertEquals("A", test.get(0));
        assertEquals("B", test.get(1));
    }

    @Test
    public void at_the_beginning() {
        final List<String> test = new ArrayList<>();
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            public void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "list", test),
                                dependency(HashMap.class, "hashmap")
                        );
                prod("prod1")
                        .is(
                                inline("list.add(\"value1\");"),
                                or(
                                        t("A"),
                                        t("B"),
                                        t("C")
                                )

                        );
            }
        });
        assertNotNull(parse(parser, "C"));
        assertEquals(1, test.size());
        assertEquals("value1", test.get(0));
    }
}
