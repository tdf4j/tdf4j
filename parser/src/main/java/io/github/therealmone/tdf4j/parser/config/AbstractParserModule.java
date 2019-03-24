package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.commons.Module;
import io.github.therealmone.tdf4j.parser.model.Production;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParserModule extends BindingMapper implements Module {
    private final List<Production> productions;
    private boolean built;

    public AbstractParserModule() {
        this.productions = new ArrayList<>();
    }

    public void build() {
        if(!built) {
            this.configure();
            this.productions.addAll(productionBindStrategy.build());
            built = true;
        }
    }

    public List<Production> getProductions() {
        return productions;
    }
}
