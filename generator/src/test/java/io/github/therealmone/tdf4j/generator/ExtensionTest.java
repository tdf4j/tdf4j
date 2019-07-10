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

package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;
import io.github.therealmone.tdf4j.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ExtensionTest extends ParserTest {
    public static final List<String> testValues = new ArrayList<>();

    @Test
    public void test() {
        final Parser parser = generate(new ParserOptions.Builder()
                .setPackage("io.github.therealmone.tdf4j.generator")
                .setClassName("ParserImpl_" + UUID.randomUUID().toString().replaceAll("-", ""))
                .setModule(new AbstractParserModule() {
                    @Override
                    protected void configure() {
                        prod("prod1")
                                .is(
                                        t("A", "ExtensionTest.testValues.add(extendedMethod());")
                                );
                    }
                })
                .setExtension(AbstractParser.class)
                .build());
        assertNotNull(parser.parse(lexer.stream("A")));
        assertEquals(1, testValues.size());
        assertEquals("SomeTestValue", testValues.get(0));
        assertTrue(parser instanceof AbstractParser);
    }

    @Test(expected = org.joor.ReflectException.class)
    public void nullable() {
        generate(new ParserOptions.Builder()
                .setPackage("io.github.therealmone.tdf4j.generator")
                .setClassName("ParserImpl_" + UUID.randomUUID().toString().replaceAll("-", ""))
                .setModule(new AbstractParserModule() {
                    @Override
                    protected void configure() {
                        prod("prod1")
                                .is(
                                        t("A", "ExtensionTest.testValues.add(extendedMethod());")
                                );
                    }
                })
                .setExtension(null)
                .build());
    }

    public static class AbstractParser {

        protected String extendedMethod() {
            return "SomeTestValue";
        }

    }
}
