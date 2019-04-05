package io.github.therealmone.tdf4j.commons.utils;

import io.github.therealmone.tdf4j.commons.model.ebnf.Follow;
import io.github.therealmone.tdf4j.commons.model.ebnf.Production;

import java.util.List;

public class FollowSetCollector {

    public Follow collect(final List<Production> productions) {
        return new Follow.Builder().build();
    }
}
