package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public abstract class Grammar {
    public abstract List<Production> productions();

    @Value.Default
    @Nullable
    public String initProduction() {
        if(productions().isEmpty()) {
            return null;
        } else {
            return productions().get(0).identifier();
        }
    }

    public abstract First firstSet();

    public abstract Follow followSet();

    public static class Builder extends ImmutableGrammar.Builder {
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(final Production production: productions()) {
            builder.append(production.toString()).append("\n");
        }
        return builder.toString();
    }
}
