package io.github.therealmone.tdf4j.parser.model.ebnf;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class Production {

    public abstract String identifier();

    public abstract List<Element> elements();

    public static class Builder extends ImmutableProduction.Builder {
        public Builder then(final Element element) {
            return super.addElements(element);
        }

        public Builder then(final Element ... elements) {
            return super.addElements(elements);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(identifier()).append(" := ");
        if(elements().size() > 0) {
            for (final Element element : elements()) {
                builder.append(element.toString()).append(",");
            }
        }
        builder.replace(builder.length() - 1, builder.length(), "");
        return builder.toString();
    }
}
