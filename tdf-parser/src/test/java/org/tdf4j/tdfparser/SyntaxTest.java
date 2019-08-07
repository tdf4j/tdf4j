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

package org.tdf4j.tdfparser;

import org.tdf4j.model.Grammar;
import org.tdf4j.model.Production;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntaxTest extends TdfParserTest {

    @Test
    public void test() {
        final Interpreter interpreter = generate("SyntaxTest.tdf");
        System.out.println(interpreter.getParserModule().build().getGrammar());
        final Grammar grammar = interpreter.getParserModule().getGrammar();

        assertEquals(8, grammar.getProductions().size());

        //[0] - or
        {
            final Production production = grammar.getProductions().get(0);
            assertEquals("or", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals("A", production.getElements().get(0).asOr().getFirst().asTerminalTag().getValue());
            assertEquals("non_terminal", production.getElements().get(0).asOr().getSecond().asNonTerminal().getValue());
        }

        //[1] - non_terminal
        {
            final Production production = grammar.getProductions().get(1);
            assertEquals("non_terminal", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals("terminal", production.getElements().get(0).asNonTerminal().getValue());
        }

        //[2] - repeat
        {
            final Production production = grammar.getProductions().get(2);
            assertEquals("repeat", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals(2, production.getElements().get(0).asRepeat().getElements().length);
            assertEquals("A", production.getElements().get(0).asRepeat().getElements()[0].asTerminalTag().getValue());
            assertEquals("non_terminal", production.getElements().get(0).asRepeat().getElements()[1].asNonTerminal().getValue());
        }

        //[3] - inline_action
        {
            final Production production = grammar.getProductions().get(3);
            assertEquals("inline_action", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals("System.out.println(\"test\");", production.getElements().get(0).asInlineAction().getCode());
        }

        //[4] - optional
        {
            final Production production = grammar.getProductions().get(4);
            assertEquals("optional", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals("A", production.getElements().get(0).asOptional().getElements()[0].asTerminalTag().getValue());
            assertEquals("non_terminal", production.getElements().get(0).asOptional().getElements()[1].asNonTerminal().getValue());
        }

        //[5] - terminal
        {
            final Production production = grammar.getProductions().get(5);
            assertEquals("terminal", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals("A", production.getElements().get(0).asTerminalTag().getValue());
        }

        //[6] - repetition
        {
            final Production production = grammar.getProductions().get(6);
            assertEquals("repetition", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals(3, production.getElements().get(0).asRepetition().getTimes());
            assertEquals("A", production.getElements().get(0).asRepetition().getElement().asGroup().getElements()[0].asTerminalTag().getValue());
            assertEquals("non_terminal", production.getElements().get(0).asRepetition().getElement().asGroup().getElements()[1].asNonTerminal().getValue());
        }

        //[7] - group
        {
            final Production production = grammar.getProductions().get(7);
            assertEquals("group", production.getIdentifier().getValue());
            assertEquals(1, production.getElements().size());
            assertEquals("A", production.getElements().get(0).asGroup().getElements()[0].asTerminalTag().getValue());
            assertEquals("non_terminal", production.getElements().get(0).asGroup().getElements()[1].asNonTerminal().getValue());
        }
    }
}
