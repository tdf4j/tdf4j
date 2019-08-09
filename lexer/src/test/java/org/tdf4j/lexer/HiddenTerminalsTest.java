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

package org.tdf4j.lexer;

import org.tdf4j.core.model.Token;
import org.tdf4j.lexer.impl.LexerImpl;
import org.tdf4j.lexer.impl.SymbolListenerImpl;
import org.tdf4j.core.module.LexerAbstractModule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HiddenTerminalsTest {

    @Test
    public void white_spaces() {
        final Lexer lexer = new LexerImpl(new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("ws").pattern("\\s|\\n|\\r").hidden(true);
            }
        }.build(), new SymbolListenerImpl());
        assertEquals(0, lexer.analyze("   \n  \r \n\r  \r\n").size());
    }

    @Test
    public void single_line_comment() {
        final Lexer lexer = new LexerImpl(new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("single_line_comment").pattern("//.*(\n|\r|\r\n|\n\r)").hidden(true);
                tokenize("VAR").pattern("[A-Z]+");
            }
        }.build(), new SymbolListenerImpl());
        final List<Token> tokens = lexer.analyze("//comment\nA//comment\rB//comment\r\nC");
        assertEquals(3, tokens.size());
        assertEquals("A", tokens.get(0).getValue());
        assertEquals("B", tokens.get(1).getValue());
        assertEquals("C", tokens.get(2).getValue());
    }

    @Test
    public void multi_line_comment() {
        final Lexer lexer = new LexerImpl(new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("multi_line_comment").pattern("/\\*[^(\\*/)]*\\*/").hidden(true);
                tokenize("VAR").pattern("[A-Z]+");
            }
        }.build(), new SymbolListenerImpl());
        final List<Token> tokens = lexer.analyze("/*comment\n\r\n\n*/A/*comment*/B/*\ncomment\n*/C");
        assertEquals(3, tokens.size());
        assertEquals("A", tokens.get(0).getValue());
        assertEquals("B", tokens.get(1).getValue());
        assertEquals("C", tokens.get(2).getValue());
    }
}
