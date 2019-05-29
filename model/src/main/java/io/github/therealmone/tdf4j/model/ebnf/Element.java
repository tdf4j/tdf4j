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
package io.github.therealmone.tdf4j.model.ebnf;

public interface Element {
    Kind kind();

    default boolean isTerminal() {
        return this.kind() == Kind.TERMINAL;
    }

    default Terminal asTerminal() {
        return (Terminal) this;
    }

    default boolean isTerminalTag() {
        return this.kind() == Kind.TERMINAL_TAG;
    }

    default Terminal.Tag asTerminalTag() {
        return (Terminal.Tag) this;
    }

    default boolean isNonTerminal() {
        return this.kind() == Kind.NON_TERMINAL;
    }

    default NonTerminal asNonTerminal() {
        return (NonTerminal) this;
    }

    default boolean isOptional() {
        return this.kind() == Kind.OPTIONAL;
    }

    default Optional asOptional() {
        return (Optional) this;
    }

    default boolean isGroup() {
        return this.kind() == Kind.GROUP;
    }

    default Group asGroup() {
        return (Group) this;
    }

    default boolean isOr() {
        return this.kind() == Kind.OR;
    }

    default Or asOr() {
        return (Or) this;
    }

    default boolean isRepeat() {
        return this.kind() == Kind.REPEAT;
    }

    default Repeat asRepeat() {
        return (Repeat) this;
    }

    default boolean isRepetition() {
        return this.kind() == Kind.REPETITION;
    }

    default Repetition asRepetition() {
        return (Repetition) this;
    }

    default boolean isInlineAction() {
        return this.kind() == Kind.INLINE_ACTION;
    }

    default InlineAction asInlineAction() {
        return (InlineAction) this;
    }

    enum Kind {
        OPTIONAL,
        OR,
        REPEAT,
        REPETITION,
        GROUP,
        TERMINAL,
        TERMINAL_TAG,
        NON_TERMINAL,
        INLINE_ACTION
    }
}
