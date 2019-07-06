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
package io.github.therealmone.tdf4j.module.parser;

import io.github.therealmone.tdf4j.model.Dependency;
import io.github.therealmone.tdf4j.model.Environment;
import io.github.therealmone.tdf4j.model.Production;
import io.github.therealmone.tdf4j.model.ebnf.*;

public interface BindMethods {
    void axiom(final String identifier);

    Production.Builder prod(final String identifier);

    Environment.Builder environment();

    <T> Dependency<T> dependency(final Class<T> clazz, final String name, final T instance);

    <T> Dependency<T> dependency(final Class<T> clazz, final String name);

    Optional optional(final Element ... elements);

    Group group(final Element ... elements);

    Repeat repeat(final Element ... elements);

    Repetition repetition(final Element element, final int times);

    Or or(final Element first, final Element second);

    Or oneOf(final Element ... elements);

    Terminal.Tag t(final String tag);

    Terminal.Tag t(final String tag, final String tokenAction);

    NonTerminal nt(final String identifier);

    NonTerminal nt(final String identifier, final String nodeAction);

    InlineAction inline(final String code);
}
