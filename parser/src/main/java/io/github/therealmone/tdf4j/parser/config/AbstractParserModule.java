package io.github.therealmone.tdf4j.parser.config;

import io.github.therealmone.tdf4j.commons.Module;
import io.github.therealmone.tdf4j.commons.model.ebnf.First;
import io.github.therealmone.tdf4j.commons.model.ebnf.Follow;
import io.github.therealmone.tdf4j.commons.model.ebnf.Grammar;
import io.github.therealmone.tdf4j.commons.model.ebnf.Production;
import io.github.therealmone.tdf4j.commons.utils.FirstSetCollector;
import io.github.therealmone.tdf4j.commons.utils.FollowSetCollector;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractParserModule extends BindingMapper implements Module {
    private final FirstSetCollector firstSetCollector = new FirstSetCollector();
    private final FollowSetCollector followSetCollector = new FollowSetCollector();
    private boolean built;
    private Grammar grammar;

    public AbstractParserModule build() {
        if(!built) {
            this.configure();
            final List<Production> productions = productionBindStrategy.build();
            this.grammar = new Grammar.Builder()
                    .addAllProductions(productions)
                    .initProduction(initProduction)
                    .firstSet(firstSetCollector.collect(productions))
                    .followSet(followSetCollector.collect(productions))
                    .build();
            built = true;
        }
        return this;
    }

    @Nonnull
    public Grammar getGrammar() {
        return grammar != null
                ? grammar
                : new Grammar.Builder()
                    .firstSet(new First.Builder().build())
                    .followSet(new Follow.Builder().build())
                    .build();
    }
}
