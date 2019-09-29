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
import java.util.List;

import static org.junit.Assert.*;

public class TokenActionTest extends ParserTest {

    @Test
    public void normal() {
        final List<String> tokens = new ArrayList<>();
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            protected void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "tokens", tokens)
                        );
                prod("prod1")
                        .is(
                                t("A", "tokens.add(token.getValue());"),
                                t("B"),
                                t("C", "tokens.add(token.getValue());")
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(2, tokens.size());
        assertEquals("A", tokens.get(0));
        assertEquals("C", tokens.get(1));
    }

    @Test
    public void nullable() {
        final List<String> tokens = new ArrayList<>();
        final Parser parser = generateParser(new ParserAbstractModule() {
            @Override
            protected void configure() {
                environment()
                        .setDependencies(
                                dependency(List.class, "tokens", tokens)
                        );
                prod("prod1")
                        .is(
                                t("A", null),
                                t("B"),
                                t("C", null)
                        );
            }
        });
        assertNotNull(parse(parser, "ABC"));
        assertEquals(0, tokens.size());
    }

}
