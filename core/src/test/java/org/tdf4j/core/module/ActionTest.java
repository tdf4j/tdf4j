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

package org.tdf4j.core.module;

import org.tdf4j.core.model.ebnf.NonTerminal;
import org.tdf4j.core.model.ebnf.Terminal;
import org.junit.Test;

import static org.junit.Assert.*;

public class ActionTest {

    @Test
    public void TokenAction() {
        new ParserAbstractModule() {
            @Override
            protected void configure() {
                final Terminal.Tag terminal = t("A", "System.out.println(token);");
                assertEquals("A", terminal.getValue());
                assertEquals("System.out.println(token);", terminal.getTokenAction());
            }
        }.build();
    }

    @Test
    public void NodeAction() {
        new ParserAbstractModule() {
            @Override
            protected void configure() {
                final NonTerminal nonTerminal = nt("A", "System.out.println(node);");
                assertEquals("a", nonTerminal.getValue());
                assertEquals("System.out.println(node);", nonTerminal.getNodeAction());
            }
        }.build();
    }

}
