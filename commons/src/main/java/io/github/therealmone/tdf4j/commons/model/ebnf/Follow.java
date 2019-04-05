package io.github.therealmone.tdf4j.commons.model.ebnf;

import org.immutables.value.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Value.Immutable
public abstract class Follow {

    @Value.Default
    public Map<Production, Set<Terminal.Tag>> set() {
        return new HashMap<>();
    }

    public static class Builder extends ImmutableFollow.Builder {
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        set().forEach((production, tags) -> {
            builder.append("follow(")
                    .append(production.identifier())
                    .append(")")
                    .append("= {");
            tags.forEach(tag -> builder.append(tag.value()).append(","));
            builder.replace(builder.length() - 1, builder.length(), "");
            builder.append("}\n");
        });
        return builder.toString();
    }
}
