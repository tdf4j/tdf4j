package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class Production {

    public abstract String identifier();

    public abstract List<Element> elements();

    @Value.Default
    public InlineAction inlineAction() {
        return new InlineAction.Builder().build();
    }

    public static class Builder extends ImmutableProduction.Builder {
        public Builder then(final Element element) {
            return super.addElements(element);
        }

        public Builder is(final Element ... elements) {
            return super.addElements(elements);
        }

        @SuppressWarnings("all")
        public Builder inline(final String code) {
            if(code == null || code.trim().equalsIgnoreCase("")) {
                throw new IllegalStateException("Code can't be blank or null");
            }
            return super.inlineAction(new InlineAction.Builder().code(code).build());
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
        if(!inlineAction().code().trim().equalsIgnoreCase("")) {
            builder.append("\n").append(inlineAction());
        }
        return builder.toString();
    }
}
