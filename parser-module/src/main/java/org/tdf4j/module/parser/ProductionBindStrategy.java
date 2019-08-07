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
package org.tdf4j.module.parser;

import org.tdf4j.model.BindStrategy;
import org.tdf4j.model.ImmutableProduction;
import org.tdf4j.model.Production;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductionBindStrategy implements BindStrategy<String, Production.Builder, List<Production>> {
    private final Map<String, Production.Builder> prods = new HashMap<>();

    @Override
    public Production.Builder bind(final String key) {
        //noinspection ConstantConditions
        if(key == null || key.trim().equals("")) {
            throw new RuntimeException("Key can't be blank or null");
        }
        if(prods.containsKey(key)) {
            throw new RuntimeException("Key " + key + "already bind");
        }
        prods.put(key, new Production.Builder().identifier(key));
        return prods.get(key);
    }

    @Override
    public List<Production> build() {
        return prods.values().stream().map(ImmutableProduction.Builder::build).collect(Collectors.toList());
    }
}
