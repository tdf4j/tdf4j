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

package org.tdf4j.module.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class InlineActionBindTest {

    @Test
    public void normal() {
        final AbstractParserModule module = new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                inline("System.out.println(\"inline action\")")
                        );
            }
        }.build();
        assertEquals("System.out.println(\"inline action\")", module.getGrammar().getProductions().get(0).getElements().get(0).asInlineAction().getCode());
        assertEquals("prod1 := <<\nSystem.out.println(\"inline action\")\n>>", module.getGrammar().getProductions().get(0).toString());
    }

    @Test(expected = IllegalStateException.class)
    public void null_code() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                inline(null)
                        );
            }
        }.build();
    }

    @Test(expected = IllegalStateException.class)
    public void blank_code() {
        new AbstractParserModule() {
            @Override
            public void configure() {
                prod("prod1")
                        .is(
                                inline("     ")
                        );
            }
        }.build();
    }
}
