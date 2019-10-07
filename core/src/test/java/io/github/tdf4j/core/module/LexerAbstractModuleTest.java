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

package io.github.tdf4j.core.module;

import io.github.tdf4j.core.model.Letter;
import io.github.tdf4j.core.model.ebnf.Terminal;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;

public class LexerAbstractModuleTest {

    @Test
    public void normal_configuration() {
        final LexerAbstractModule config = new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("ONE").pattern("^one$");
                tokenize("TWO").pattern("^two$");
                tokenize("THREE").pattern("^three$");
            }
        }.build();

        final List<Letter> letters = config.getAlphabet().getLetters();
        assertEquals(3, letters.size());
        Assert.assertEquals("ONE", letters.get(0).getTag().getValue());
        Assert.assertEquals("^one$", letters.get(0).getPattern().pattern());
        Assert.assertEquals("TWO", letters.get(1).getTag().getValue());
        Assert.assertEquals("^two$", letters.get(1).getPattern().pattern());
        Assert.assertEquals("THREE", letters.get(2).getTag().getValue());
        Assert.assertEquals("^three$", letters.get(2).getPattern().pattern());
    }

    @Test(expected = RuntimeException.class)
    public void not_complete_bindings() {
        new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("pattern");
                tokenize("token2").pattern("pattern2");
                tokenize("token3");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void duplicate_names() {
        new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("pattern");
                tokenize("token2").pattern("pattern2");
                tokenize("token2").pattern("pattern3");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void null_name() {
        new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize((Terminal) null).pattern("pattern");
            }
        }.build();
    }

    @Test(expected = RuntimeException.class)
    public void blank_name() {
        new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("    ").pattern("pattern");
            }
        }.build();
    }

    @Test(expected = PatternSyntaxException.class)
    public void not_compilable_pattern() {
        new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("[a]{invalid}");
            }
        }.build();
    }

    @Test
    public void priority() {
        final LexerAbstractModule config = new LexerAbstractModule() {
            @Override
            public void configure() {
                tokenize("token").pattern("pattern").priority(100);
            }
        }.build();
        assertEquals(1, config.getAlphabet().getLetters().size());
        final Letter letter = config.getAlphabet().getLetters().get(0);
        assertEquals("TOKEN", letter.getTag().getValue());
        assertEquals("pattern", letter.getPattern().pattern());
        assertEquals(100, letter.priority());
    }
}
