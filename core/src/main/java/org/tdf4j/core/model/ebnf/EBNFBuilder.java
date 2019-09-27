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

import javax.annotation.Nullable;

public final class EBNFBuilder {

    public static Terminal terminal(final String value) {
        return new Terminal.Builder().setValue(value).build();
    }

    public static Terminal terminal(final String value, @Nullable final String tokenAction) {
        return new Terminal.Builder().setValue(value).setTokenAction(tokenAction).build();
    }

    public static Alternative alternative(final int index, final Element element) {
        return new Alternative.Builder().setIndex(index).setElement(element).build();
    }

    public static Group group(final Element... elements) {
        return new Group.Builder().setElements(elements).build();
    }

    public static InlineAction inlineAction(final String code) {
        return new InlineAction.Builder().setCode(code).build();
    }

    public static NonTerminal nonTerminal(final String value) {
        return new NonTerminal.Builder().setValue(value).build();
    }

    public static NonTerminal nonTerminal(final String value, @Nullable final String nodeAction) {
        return new NonTerminal.Builder().setValue(value).setNodeAction(nodeAction).build();
    }

    public static Optional optional(final Element... elements) {
        return new Optional.Builder().setElements(elements).build();
    }

    public static Or or(final Element... elements) {
        return new Or.Builder().addAlternatives(elements).build();
    }

    public static Repeat repeat(final Element... elements) {
        return new Repeat.Builder().setElements(elements).build();
    }

    public static Repetition repetition(final Element element, final int times) {
        return new Repetition.Builder().setTimes(times).setElement(element).build();
    }

}
