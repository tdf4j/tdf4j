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
import org.tdf4j.core.model.ImmutableLetter;
import org.tdf4j.core.model.Letter;
import org.tdf4j.core.model.ebnf.Terminal;

import java.util.*;
import java.util.stream.Collectors;

public class LetterBindStrategy implements BindStrategy<Terminal, Letter.Builder, Alphabet> {
    private final Map<Terminal, Letter.Builder> builders = new LinkedHashMap<>();

    @Override
    public Letter.Builder bind(final Terminal key) {
        //noinspection ConstantConditions
        if(key == null || key.getValue().trim().equals("")) {
            throw new RuntimeException("Tag can't be null or blank");
        }
        if(builders.containsKey(key)) {
            throw new RuntimeException("Key " + key + " already bind");
        }
        builders.put(key, new Letter.Builder().tag(key));
        return builders.get(key);
    }

    @Override
    public Alphabet build() {
        return new Alphabet.Builder().addAllLetters(
                builders.values().stream().map(ImmutableLetter.Builder::build).collect(Collectors.toList())
        ).build();
    }
}
