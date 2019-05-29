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
package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.generator.templates.Buildable;

import javax.annotation.Nullable;

public interface CodeBlock extends Buildable {

    @Nullable
    static CodeBlock fromElement(final Element element) {
        switch (element.kind()) {
            case TERMINAL_TAG:
                return new TerminalTagTemplate.Builder().terminalTag(element.asTerminalTag()).build();
            case NON_TERMINAL:
                return new NonTerminalTemplate.Builder().nonTerminal(element.asNonTerminal()).build();
            case OPTIONAL:
                return new OptionalTemplate.Builder().optional(element.asOptional()).build();
            case OR:
                return new OrTemplate.Builder().or(element.asOr()).build();
            case REPEAT:
                return new RepeatTemplate.Builder().repeat(element.asRepeat()).build();
            case REPETITION:
                return new RepetitionTemplate.Builder().repetition(element.asRepetition()).build();
            case GROUP:
                return new GroupTemplate.Builder().group(element.asGroup()).build();
            case INLINE_ACTION:
                return new InlineActionTemplate.Builder().inline(element.asInlineAction()).build();
            default:
                return null;
        }
    }
}
