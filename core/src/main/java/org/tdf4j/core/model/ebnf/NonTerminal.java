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
package org.tdf4j.core.model.ebnf;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public abstract class NonTerminal extends AbstractElement {
    @Override
    public Kind kind() {
        return Kind.NON_TERMINAL;
    }

    public abstract String getValue();

    @Nullable
    @Value.Default
    @Value.Auxiliary
    public String getNodeAction() {
        return null;
    }

    @Value.Check
    NonTerminal normalize() {
        final char[] value = getValue().toCharArray();
        for(final char ch : value) {
            if(Character.isLetter(ch) && !Character.isLowerCase(ch)) {
                return new NonTerminal.Builder()
                        .setValue(getValue().toLowerCase())
                        .setNodeAction(getNodeAction())
                        .build();
            }
        }
        return this;
    }

    public static class Builder extends ImmutableNonTerminal.Builder {
    }

    @Override
    public String toString() {
        return getValue();
    }
}
