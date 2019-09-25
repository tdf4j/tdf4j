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

package org.tdf4j.core.utils;

import org.tdf4j.core.model.ebnf.Element;
import org.tdf4j.core.model.ebnf.Group;
import org.tdf4j.core.model.ebnf.Terminal;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElementsUtilTest {

    @Test
    public void to_string_group() {
        assertEquals("(A,B,C)", Elements.convertToString(
                new Group() {
                    @Override
                    public Element[] getElements() {
                        return new Element[] {
                                new Terminal.Builder().setValue("A").build(),
                                new Terminal.Builder().setValue("B").build(),
                                new Terminal.Builder().setValue("C").build()
                        };
                    }
                }
        ));
    }

    @Test
    public void to_string_empty_group() {
        assertEquals("", Elements.convertToString());
    }

    @Test
    public void to_string_with_separator() {
        assertEquals("A|||B|||C", Elements.convertToString( "|||",
                new Terminal.Builder().setValue("A").build(),
                new Terminal.Builder().setValue("B").build(),
                new Terminal.Builder().setValue("C").build()
        ));
    }
}
