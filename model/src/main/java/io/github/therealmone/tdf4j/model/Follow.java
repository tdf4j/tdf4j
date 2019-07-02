/*
 * Copyright 2019 Roman Fatnev
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
package io.github.therealmone.tdf4j.model;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import org.immutables.value.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Value.Immutable
public abstract class Follow {

    @Value.Default
    public Map<Production, Set<Terminal.Tag>> getSet() {
        return new HashMap<>();
    }

    public static class Builder extends ImmutableFollow.Builder {
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        getSet().forEach((production, tags) -> {
            builder.append("follow(")
                    .append(production.getIdentifier())
                    .append(")")
                    .append("= {");
            tags.forEach(tag -> builder.append(tag.getValue()).append(","));
            builder.replace(builder.length() - 1, builder.length(), "");
            builder.append("}\n");
        });
        return builder.toString();
    }
}
