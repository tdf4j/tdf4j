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
package io.github.tdf4j.core.model.ebnf;

import org.immutables.value.Value;

import static io.github.tdf4j.core.model.ebnf.Elements.*;

@Value.Immutable
public abstract class Group implements Element {
    @Override
    public Kind kind() {
        return Kind.GROUP;
    }

    public abstract Element[] getElements();

    public static class Builder extends ImmutableGroup.Builder {
    }

    @Override
    public String toString() {
        return "(" + convertToString(getElements()) + ")";
    }
}
