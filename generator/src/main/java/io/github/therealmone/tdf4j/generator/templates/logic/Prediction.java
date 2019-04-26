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

import io.github.therealmone.tdf4j.commons.model.ebnf.Element;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public interface Prediction {
    @Nonnull
    default List<String> getStartElements(final Element element) {
        switch (element.kind()) {
            case REPEAT:
                return element.asRepeat().elements().length == 0
                        ? new ArrayList<>()
                        : getStartElements(element.asRepeat().elements()[0]);

            case REPETITION:
                return element.asRepetition().element() == null
                        ? new ArrayList<>()
                        : getStartElements(element.asRepetition().element());

            case OR:
                return new ArrayList<>() {{
                    addAll(getStartElements(element.asOr().first()));
                    addAll(getStartElements(element.asOr().second()));
                }};

            case GROUP:
                return element.asGroup().elements().length == 0
                        ? new ArrayList<>()
                        : getStartElements(element.asGroup().elements()[0]);

            case OPTIONAL:
                return element.asOptional().elements().length == 0
                        ? new ArrayList<>()
                        : getStartElements(element.asOptional().elements()[0]);

            case TERMINAL_TAG:
                return new ArrayList<>() {{add(element.asTerminalTag().value());}};

            case NON_TERMINAL:
                return new ArrayList<>() {{add(element.asNonTerminal().identifier());}};

            default: return new ArrayList<>();
        }
    }
}
