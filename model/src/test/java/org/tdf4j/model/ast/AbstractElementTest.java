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

package org.tdf4j.model.ast;

import org.tdf4j.model.ebnf.AbstractElement;
import org.tdf4j.model.ebnf.Element;
import org.tdf4j.model.ebnf.Group;
import org.tdf4j.model.ebnf.Terminal;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractElementTest {

    @Test
    public void to_string_group() {
        final AET aet = new AET();
        assertEquals("(A,B,C)", aet.toStringGroup(
                new Group() {
                    @Override
                    public Element[] getElements() {
                        return new Element[] {
                                new Terminal.Tag.Builder().setValue("A").build(),
                                new Terminal.Tag.Builder().setValue("B").build(),
                                new Terminal.Tag.Builder().setValue("C").build()
                        };
                    }
                }
        ));
    }

    @Test
    public void to_string_empty_group() {
        final AET aet = new AET();
        assertEquals("", aet.toStringGroup());
    }

    class AET extends AbstractElement {
        @Override
        public Kind kind() {
            return null;
        }

        @Override
        public String toStringGroup(final Element... elements) {
            return super.toStringGroup(elements);
        }
    }
}
