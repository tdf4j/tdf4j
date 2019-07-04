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
package io.github.therealmone.tdf4j.utils;

import io.github.therealmone.tdf4j.model.First;
import io.github.therealmone.tdf4j.model.Production;
import io.github.therealmone.tdf4j.model.ebnf.*;

import javax.annotation.Nullable;
import java.util.*;

public class FirstSetCollector {

    public First collect(final List<Production> productions) {
        final Context context = new Context(productions);
        for(final Production production : productions) {
            firstOf(context, production);
        }
        return new First.Builder().setSet(context.getSet()).build();
    }

    private List<Terminal.Tag> firstOf(final Context context, @Nullable final Production production) {
        if(production == null) {
            return Collections.emptyList();
        }
        if (!context.getSet().containsKey(production.getIdentifier())) {
            context.getSet().put(production.getIdentifier(), new HashSet<>());
        }
        if (!production.getElements().isEmpty()) {
            context.getSet().get(production.getIdentifier()).addAll(
                    firstOf(context, production.getIdentifier(), firstElement(production.getElements()))
            );
        }
        return new ArrayList<>(context.getSet().get(production.getIdentifier()));
    }

    private List<Terminal.Tag> firstOf(final Context context, final NonTerminal currentNT, @Nullable final Element element) {
        if(element == null || element.kind() == null) {
            return Collections.emptyList();
        }

        switch (element.kind()) {

            case GROUP: {
                return element.asGroup().getElements().length == 0
                        ? Collections.emptyList()
                        : firstOf(context, currentNT, firstElement(Arrays.asList(element.asGroup().getElements())));
            }

            case NON_TERMINAL: {
                return element.asNonTerminal().getValue().equalsIgnoreCase(currentNT.getValue())
                        ? Collections.emptyList()
                        : firstOf(context, context.getProduction(element.asNonTerminal()));
            }

            case OPTIONAL: {
                return element.asOptional().getElements().length == 0
                        ? Collections.emptyList()
                        : firstOf(context, currentNT, firstElement(Arrays.asList(element.asOptional().getElements())));
            }

            case OR: {
                return new ArrayList<>() {{
                    addAll(firstOf(context, currentNT, firstElement(Collections.singletonList(element.asOr().getFirst()))));
                    addAll(firstOf(context, currentNT, firstElement(Collections.singletonList(element.asOr().getSecond()))));
                }};
            }

            case REPEAT: {
                return element.asRepeat().getElements().length == 0
                        ? Collections.emptyList()
                        : firstOf(context, currentNT, firstElement(Arrays.asList(element.asRepeat().getElements())));
            }

            case REPETITION: {
                return firstOf(context, currentNT, firstElement(Collections.singletonList(element.asRepetition().getElement())));
            }

            case TERMINAL_TAG: {
                return new ArrayList<>() {{
                    add(element.asTerminalTag());
                }};
            }

            default: return Collections.emptyList();
        }
    }

    @Nullable
    Element firstElement(final List<Element> elements) {
        for(final Element element : elements) {
            if(element == null || element.isInlineAction()) {
                continue;
            }
            return element;
        }
        return null;
    }

    static class Context {
        private final List<Production> productions;
        private final Map<NonTerminal, Set<Terminal.Tag>> set;

        Context(final List<Production> productions) {
            this.productions = productions;
            this.set = new HashMap<>();
        }

        Map<NonTerminal, Set<Terminal.Tag>> getSet() {
            return set;
        }

        @Nullable
        Production getProduction(final NonTerminal ident) {
            for(final Production production : productions) {
                if(production.getIdentifier().equals(ident)) {
                    return production;
                }
            }
            return null;
        }
    }
}
