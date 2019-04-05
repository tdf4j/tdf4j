package io.github.therealmone.tdf4j.commons.utils;

import io.github.therealmone.tdf4j.commons.model.ebnf.*;

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
                    firstOf(context, production.identifier(), production.elements().get(0))
            );
        }
        return new ArrayList<>(context.getSet().get(production.identifier()));
    }

    private List<Terminal.Tag> firstOf(final Context context, final String currentNT, final Element element) {
        switch (element.kind()) {

            case EXCEPT:
                return new ArrayList<>();

            case GROUP: {
                return element.asGroup().elements().length == 0
                        ? new ArrayList<>()
                        : firstOf(context, currentNT, element.asGroup().elements()[0]);
            }

            case NON_TERMINAL: {
                return element.asNonTerminal().identifier().equalsIgnoreCase(currentNT)
                        ? new ArrayList<>()
                        : firstOf(context, context.getProduction(element.asNonTerminal().identifier()));
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

    private class Context {
        private final List<Production> productions;
        private final Map<String, Set<Terminal.Tag>> set;

        Context(final List<Production> productions) {
            this.productions = productions;
            this.set = new HashMap<>();
        }

        Map<String, Set<Terminal.Tag>> getSet() {
            return set;
        }

        @Nullable
        Production getProduction(final String ident) {
            for(final Production production : productions) {
                if(production.identifier().equalsIgnoreCase(ident)) {
                    return production;
                }
            }
            return null;
        }
    }
}
