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
package org.tdf4j.model;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public abstract class Grammar {

    public abstract List<Production> getProductions();

    @Value.Default
    @Nullable
    public String getAxiom() {
        if(getProductions().isEmpty()) {
            return null;
        } else {
            return getProductions().get(0).getIdentifier().toString();
        }
    }

    public abstract First getFirstSet();

    public abstract Follow getFollowSet();

    public static class Builder extends ImmutableGrammar.Builder {
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(final Production production: getProductions()) {
            builder.append(production.toString()).append("\n");
        }
        return builder.toString();
    }
}
