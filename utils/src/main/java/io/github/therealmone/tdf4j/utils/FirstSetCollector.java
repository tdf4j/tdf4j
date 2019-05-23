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

import io.github.therealmone.tdf4j.model.ebnf.*;

import javax.annotation.Nullable;
import java.util.*;

public class FirstSetCollector {

    public First collect(final List<Production> productions) {
        final Context context = new Context(productions);
        for(final Production production : productions) {
            firstOf(context, production);
        }
        return new First.Builder().set(context.getSet()).build();
    }

    private List<Terminal.Tag> firstOf(final Context context, @Nullable final Production production) {
        if(production == null) {
            return new ArrayList<>();
        }
        if (!context.getSet().containsKey(production.identifier())) {
            context.getSet().put(production.identifier(), new HashSet<>());
        }
        if (!production.elements().isEmpty()) {
            context.getSet().get(production.identifier()).addAll(
                    firstOf(context, production.identifier(), firstElement(production))
            );
        }
        return new ArrayList<>(context.getSet().get(production.identifier()));
    }

    private List<Terminal.Tag> firstOf(final Context context, final NonTerminal currentNT, @Nullable final Element element) {
        if(element == null) {
            return new ArrayList<>();
        }

        switch (element.kind()) {

            case EXCEPT:
                return new ArrayList<>();

            case GROUP: {
                return element.asGroup().elements().length == 0
                        ? new ArrayList<>()
                        : firstOf(context, currentNT, element.asGroup().elements()[0]);
            }

            case NON_TERMINAL: {
                return element.asNonTerminal().identifier().equalsIgnoreCase(currentNT.identifier())
                        ? new ArrayList<>()
                        : firstOf(context, context.getProduction(element.asNonTerminal()));
            }

            case OPTIONAL: {
                return element.asOptional().elements().length == 0
                        ? new ArrayList<>()
                        : firstOf(context, currentNT, element.asOptional().elements()[0]);
            }

            case OR: {
                return new ArrayList<>() {{
                    addAll(firstOf(context, currentNT, element.asOr().first()));
                    addAll(firstOf(context, currentNT, element.asOr().second()));
                }};
            }

            case REPEAT: {
                return element.asRepeat().elements().length == 0
                        ? new ArrayList<>()
                        : firstOf(context, currentNT, element.asRepeat().elements()[0]);
            }

            case REPETITION: {
                return element.asRepetition().element() == null
                        ? new ArrayList<>()
                        : firstOf(context, currentNT, element.asRepetition().element());
            }

            case TERMINAL_TAG: {
                return new ArrayList<>() {{
                    add(element.asTerminalTag());
                }};
            }


            default: return new ArrayList<>();
        }
    }

    @Nullable
    private Element firstElement(final Production production) {
        for(final Element element : production.elements()) {
            if(element.isInlineAction()) {
                continue;
            }
            return element;
        }
        return null;
    }

    private class Context {
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
                if(production.identifier().equals(ident)) {
                    return production;
                }
            }
            return null;
        }
    }
}
