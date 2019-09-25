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
public abstract class Terminal implements Element {

    @Override
    public Kind kind() {
        return Kind.TERMINAL;
    }

    public abstract String getValue();

    @Nullable
    @Value.Auxiliary
    @Value.Default
    public String getTokenAction() {
        return null;
    }

    @Value.Check
    Terminal normalize() {
        final char[] value = getValue().toCharArray();
        for(final char ch : value) {
            if(Character.isLetter(ch) && !Character.isUpperCase(ch)) {
                return new Terminal.Builder()
                        .setValue(getValue().toUpperCase())
                        .setTokenAction(getTokenAction())
                        .build();
            }
        }
        return this;
    }

    public static class Builder extends ImmutableTerminal.Builder {

    }

    @Override
    public String toString() {
        return getValue();
    }

}
