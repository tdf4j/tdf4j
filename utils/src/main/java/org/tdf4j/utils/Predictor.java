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
package org.tdf4j.utils;

import org.tdf4j.model.Token;
import org.tdf4j.model.First;
import org.tdf4j.model.Follow;
import org.tdf4j.model.ebnf.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Predictor {
    private final First first;
    private final Follow follow;
    final Map<Terminal.Tag, List<String>> cache = new HashMap<>();

    public Predictor(final First first, final Follow follow) {
        this.first = first;
        this.follow = follow;
    }

    public List<String> predict(final Token token) {
        //noinspection ConstantConditions
        if(token == null) {
            return new ArrayList<>();
        }
        if(!cache.containsKey(token.getTag())) {
            final List<String> predictions = new ArrayList<>();
            first.getSet().forEach((ident, set) -> {
                if(set.contains(token.getTag())) {
                    predictions.add(ident.getValue());
                }
            });
            cache.put(token.getTag(), predictions);
            predictions.add(token.getTag().getValue());
        }
        return cache.get(token.getTag());
    }
}
