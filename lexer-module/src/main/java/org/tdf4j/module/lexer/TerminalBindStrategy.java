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
package org.tdf4j.module.lexer;

import org.tdf4j.model.BindStrategy;
import org.tdf4j.model.ebnf.ImmutableTerminal;
import org.tdf4j.model.ebnf.Terminal;

import java.util.*;
import java.util.stream.Collectors;

public class TerminalBindStrategy implements BindStrategy<String, Terminal.Builder, List<Terminal>> {
    private final Map<String, Terminal.Builder> builders = new HashMap<>();

    @Override
    public Terminal.Builder bind(final String key) {
        //noinspection ConstantConditions
        if(key == null || key.trim().equals("")) {
            throw new RuntimeException("Tag can't be null or blank");
        }
        if(builders.containsKey(key)) {
            throw new RuntimeException("Key " + key + " already bind");
        }
        builders.put(key, new Terminal.Builder().tag(key));
        return builders.get(key);
    }

    @Override
    public List<Terminal> build() {
        return builders.values().stream().map(ImmutableTerminal.Builder::build).collect(Collectors.toList());
    }
}
