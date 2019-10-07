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

package io.github.tdf4j.generator;

import io.github.tdf4j.lexer.Lexer;
import io.github.tdf4j.core.model.Stream;
import io.github.tdf4j.core.model.Token;
import io.github.tdf4j.core.module.LexerAbstractModule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CharSequenceLexicalAnalyzeTest {
    private final Lexer lexer = Lexer.get(new LexerAbstractModule() {
        @Override
        protected void configure() {
            tokenize("A").pattern("A");
            tokenize("B").pattern("B");
            tokenize("C").pattern("C");
        }
    });


    @Test
    public void from_string() {
        //analyze
        {
            final List<Token> tokens = lexer.analyze("ABC").toList();
            assertEquals(3, tokens.size());
            assertEquals("A", tokens.get(0).getTag().getValue());
            assertEquals("A", tokens.get(0).getValue());
            assertEquals("B", tokens.get(1).getTag().getValue());
            assertEquals("B", tokens.get(1).getValue());
            assertEquals("C", tokens.get(2).getTag().getValue());
            assertEquals("C", tokens.get(2).getValue());
        }

        //stream
        {
            final Stream<Token> tokens = lexer.analyze("ABC");
            assertEquals("A", tokens.next().getValue());
            assertEquals("B", tokens.next().getValue());
            assertEquals("C", tokens.next().getValue());
        }
    }

    @Test
    public void from_string_builder() {
        //analyze
        {
            final List<Token> tokens = lexer.analyze(new StringBuilder("ABC")).toList();
            assertEquals(3, tokens.size());
            assertEquals("A", tokens.get(0).getTag().getValue());
            assertEquals("A", tokens.get(0).getValue());
            assertEquals("B", tokens.get(1).getTag().getValue());
            assertEquals("B", tokens.get(1).getValue());
            assertEquals("C", tokens.get(2).getTag().getValue());
            assertEquals("C", tokens.get(2).getValue());
        }

        //stream
        {
            final Stream<Token> tokens = lexer.analyze(new StringBuilder("ABC"));
            assertEquals("A", tokens.next().getValue());
            assertEquals("B", tokens.next().getValue());
            assertEquals("C", tokens.next().getValue());
        }
    }

    @Test
    public void from_string_buffer() {
        //analyze
        {
            final List<Token> tokens = lexer.analyze(new StringBuffer("ABC")).toList();
            assertEquals(3, tokens.size());
            assertEquals("A", tokens.get(0).getTag().getValue());
            assertEquals("A", tokens.get(0).getValue());
            assertEquals("B", tokens.get(1).getTag().getValue());
            assertEquals("B", tokens.get(1).getValue());
            assertEquals("C", tokens.get(2).getTag().getValue());
            assertEquals("C", tokens.get(2).getValue());
        }

        //stream
        {
            final Stream<Token> tokens = lexer.analyze(new StringBuffer("ABC"));
            assertEquals("A", tokens.next().getValue());
            assertEquals("B", tokens.next().getValue());
            assertEquals("C", tokens.next().getValue());
        }
    }

}
