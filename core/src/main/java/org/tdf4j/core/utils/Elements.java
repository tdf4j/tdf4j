package org.tdf4j.core.utils;

import org.tdf4j.core.model.ebnf.Alternative;
import org.tdf4j.core.model.ebnf.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Elements {

    public static String convertToString(final Element... elements) {
        return convertToString(",", elements);
    }

    public static String convertToString(final String separator, final Element... elements) {
        final StringBuilder builder = new StringBuilder();
        if(elements.length > 0) {
            for (final Element element : elements) {
                builder.append(element.toString()).append(separator);
            }
            builder.replace(builder.length() - separator.length(), builder.length(), "");
        }
        return builder.toString();
    }

    @Nonnull
    public static List<String> getStartElements(@Nullable final Element element) {
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

            case GROUP:
                return element.asGroup().getElements().length == 0
                        ? Collections.emptyList()
                        : getStartElements(firstNotInlineElement(element.asGroup().getElements()));

            case OPTIONAL:
                return element.asOptional().getElements().length == 0
                        ? Collections.emptyList()
                        : getStartElements(firstNotInlineElement(element.asOptional().getElements()));

            case TERMINAL:
                return new ArrayList<String>() {{add(element.asTerminal().getValue());}};

            case NON_TERMINAL:
                return new ArrayList<String>() {{add(element.asNonTerminal().getValue());}};

            case OR:
                return new ArrayList<String>() {{
                    if(element.asOr().getAlternatives() != null) {
                        for (final Alternative alt : element.asOr().getAlternatives()) {
                            addAll(getStartElements(firstNotInlineElement(alt)));
                        }
                    }
                }};

            case ALTERNATIVE:
                return getStartElements(element.asAlternative().getElement());

            default: return Collections.emptyList();
        }
    }

    @Nullable
    public static Element firstNotInlineElement(final Element... elements) {
        for(final Element element : elements) {
            if(element == null || element.isInlineAction()) {
                continue;
            }
            return element;
        }
        return null;
    }

}
