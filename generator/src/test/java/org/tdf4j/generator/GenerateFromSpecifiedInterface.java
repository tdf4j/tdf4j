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

import static org.junit.Assert.*;

public class GenerateFromSpecifiedInterface extends ParserTest {
    public interface TestInterface extends Parser {
        String getTestString();
    }

    @Test
    public void normal() {
        final TestInterface parser = generate(new ParserAbstractModule() {
            @Override
            public void configure() {
                environment().setCode(
                        "@Override\n" +
                                "public String getTestString() {\n" +
                                "   return \"TestValue\";\n" +
                                "}\n"
                );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        }, TestInterface.class);
        assertEquals("TestValue", parser.getTestString());
        assertNotNull(parse(parser, "ABC"));
    }

    @Test(expected = NullPointerException.class)
    public void null_interface() {
        generate(new ParserAbstractModule() {
            @Override
            public void configure() {
                environment().setCode(
                        "@Override\n" +
                                "public String getTestString() {\n" +
                                "   return \"TestValue\";\n" +
                                "}\n"
                );
                prod("prod1")
                        .is(
                                t("A"),
                                t("B"),
                                t("C")
                        );
            }
        }, null);
    }
}
