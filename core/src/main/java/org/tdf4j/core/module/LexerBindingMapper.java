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

import org.tdf4j.core.model.Alphabet;
import org.tdf4j.core.model.Letter;
import org.tdf4j.core.model.ebnf.Terminal;

import static org.tdf4j.core.model.ebnf.EBNFBuilder.terminal;

public abstract class LexerBindingMapper implements LexerBindMethods {
    final BindStrategy<Terminal, Letter.Builder, Alphabet> letterBindStrategy = new LetterBindStrategy();

    @Override
    public Letter.Builder tokenize(final String tag) {
        return letterBindStrategy.bind(terminal(tag));
    }

    @Override
    public Letter.Builder tokenize(final Terminal tag) {
        return letterBindStrategy.bind(tag);
    }
}
