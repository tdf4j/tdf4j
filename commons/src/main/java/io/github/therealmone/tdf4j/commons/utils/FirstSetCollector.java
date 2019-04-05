package io.github.therealmone.tdf4j.commons.utils;

import io.github.therealmone.tdf4j.commons.model.ebnf.First;
import io.github.therealmone.tdf4j.commons.model.ebnf.Production;

import java.util.List;

public class FirstSetCollector {

    public First collect(final List<Production> productions) {
        return new First.Builder().build();
    }
}
