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

package io.github.therealmone.tdf4j.model.ebnf;

import org.junit.Test;

import static org.junit.Assert.*;

public class NormalizationTest {

    @Test
    public void terminal() {
        final Terminal.Tag terminal = new Terminal.Tag.Builder()
                .setValue("this_is_1_terminal")
                .setTokenAction("token.action")
                .build();
        assertNotEquals("this_is_1_terminal", terminal.getValue());
        assertEquals("THIS_IS_1_TERMINAL", terminal.getValue());
        assertEquals("token.action", terminal.getTokenAction());
    }

    @Test
    public void non_terminal() {
        final NonTerminal nonTerminal = new NonTerminal.Builder()
                .setValue("1_NON_TERMINAL")
                .setNodeAction("node.action")
                .build();
        assertNotEquals("1_NON_TERMINAL", nonTerminal.getValue());
        assertEquals("1_non_terminal", nonTerminal.getValue());
        assertEquals("node.action", nonTerminal.getNodeAction());
    }

}