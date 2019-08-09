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
package org.tdf4j.generator.templates.adaptor;

import org.tdf4j.core.model.ebnf.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Prediction {

    @Nonnull
    protected List<String> getStartElements(@Nullable final Element element) {
        if(element == null) {
            return Collections.emptyList();
        }

        switch (element.kind()) {
            case REPEAT:
                return element.asRepeat().getElements().length == 0
                        ? Collections.emptyList()
                        : getStartElements(firstNotInlineElement(element.asRepeat().getElements()));

            case REPETITION:
                return element.asRepetition().getElement() == null
                        ? Collections.emptyList()
                        : getStartElements(firstNotInlineElement(element.asRepetition().getElement()));

            case OR:
                return new ArrayList<>() {{
                    addAll(getStartElements(firstNotInlineElement(element.asOr().getFirst())));
                    addAll(getStartElements(firstNotInlineElement(element.asOr().getSecond())));
                }};

            case GROUP:
                return element.asGroup().getElements().length == 0
                        ? Collections.emptyList()
                        : getStartElements(firstNotInlineElement(element.asGroup().getElements()));

            case OPTIONAL:
                return element.asOptional().getElements().length == 0
                        ? Collections.emptyList()
                        : getStartElements(firstNotInlineElement(element.asOptional().getElements()));

            case TERMINAL_TAG:
                return new ArrayList<>() {{add(element.asTerminalTag().getValue());}};

            case NON_TERMINAL:
                return new ArrayList<>() {{add(element.asNonTerminal().getValue());}};

            default: return Collections.emptyList();
    }
    }

    @Nullable
    protected Element firstNotInlineElement(final Element... elements) {
        for(final Element element : elements) {
            if(element == null || element.isInlineAction()) {
                continue;
            }
            return element;
        }
        return null;
    }
}
