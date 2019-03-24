package io.github.therealmone.tdf4j.parser.model;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface Production {

    String identifier();

    List<String> elements();

    class Builder extends ImmutableProduction.Builder {
        public Builder then(final String element) {
            return super.addElements(element);
        }

        public Builder then(final String ... elements) {
            return super.addElements(elements);
        }
    }
}
