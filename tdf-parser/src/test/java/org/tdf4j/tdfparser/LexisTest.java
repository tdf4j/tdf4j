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

import org.tdf4j.core.model.Letter;
import org.junit.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class LexisTest extends TdfParserTest {

    @Test
    public void test() {
        final Interpreter interpreter = generate("LexisTest.tdf");
        final List<Letter> letters = interpreter.getLexerModule().build().getAlphabet().getLetters();
        System.out.println(letters);

        assertEquals(33, letters.size());
        assertTerminal(letters.get(0), letter("VAR").pattern("[a-z]+"));
        assertTerminal(letters.get(1), letter("STRING").pattern("\"[^\"]*\""));
        assertTerminal(letters.get(2), letter("NEW").pattern("new").priority(1));
        assertTerminal(letters.get(3), letter("TYPEOF").pattern("typeof").priority(1));
        assertTerminal(letters.get(4), letter("HASHSET").pattern("hashset").priority(1));
        assertTerminal(letters.get(5), letter("ARRAYLIST").pattern("arraylist").priority(1));
        assertTerminal(letters.get(6), letter("GET").pattern("get").priority(1));
        assertTerminal(letters.get(7), letter("SIZE").pattern("size").priority(1));
        assertTerminal(letters.get(8), letter("PUT").pattern("put").priority(1));
        assertTerminal(letters.get(9), letter("REMOVE").pattern("remove").priority(1));
        assertTerminal(letters.get(10), letter("REWRITE").pattern("rewrite").priority(1));
        assertTerminal(letters.get(11), letter("PRINT").pattern("print").priority(1));
        assertTerminal(letters.get(12), letter("COMMA").pattern(","));
        assertTerminal(letters.get(13), letter("CONCAT").pattern("\\+\\+").priority(2));
        assertTerminal(letters.get(14), letter("QUOTE").pattern("\""));
        assertTerminal(letters.get(15), letter("DIGIT").pattern("-?(0|([1-9][0-9]*))").priority(1));
        assertTerminal(letters.get(16), letter("DOUBLE").pattern("-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))"));
        assertTerminal(letters.get(17), letter("ASSIGN_OP").pattern("="));
        assertTerminal(letters.get(18), letter("OP").pattern("[\\+\\-/\\*]|(div)|(mod)").priority(1));
        assertTerminal(letters.get(19), letter("DEL").pattern(";", Pattern.MULTILINE));
        assertTerminal(letters.get(20), letter("WHILE").pattern("while").priority(1));
        assertTerminal(letters.get(21), letter("IF").pattern("if").priority(1));
        assertTerminal(letters.get(22), letter("ELSE").pattern("else").priority(1));
        assertTerminal(letters.get(23), letter("DO").pattern("do").priority(1));
        assertTerminal(letters.get(24), letter("FOR").pattern("for").priority(1));
        assertTerminal(letters.get(25), letter("LOP").pattern("[&\\|\\^\\!]").priority(1));
        assertTerminal(letters.get(26), letter("COP").pattern("[<>]|(<=)|(>=)|(==)|(!=)").priority(1));
        assertTerminal(letters.get(27), letter("LB").pattern("\\("));
        assertTerminal(letters.get(28), letter("RB").pattern("\\)"));
        assertTerminal(letters.get(29), letter("FLB").pattern("\\{"));
        assertTerminal(letters.get(30), letter("FRB").pattern("\\}"));
        assertTerminal(letters.get(31), letter("EOF").pattern("\\$"));
        assertTerminal(letters.get(32), letter("WS").pattern("\\s|\\n|\\r").priority(3).hidden(true));

    }

    private void assertTerminal(final Letter terminal, final Letter.Builder another) {
        final Letter expected = another.build();
        assertEquals(expected.getTag().getValue(), terminal.getTag().getValue());
        assertEquals(expected.getPattern().pattern(), terminal.getPattern().pattern());
        assertEquals(expected.getPattern().flags(), terminal.getPattern().flags());
        assertEquals(expected.priority(), terminal.priority());
        assertEquals(expected.hidden(), terminal.hidden());
    }

    private Letter.Builder letter(final String tag) {
        return new Letter.Builder().tag(tag);
    }

}
