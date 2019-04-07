package io.github.therealmone.tdf4j.parser;

import org.immutables.value.Value;

@Value.Immutable
public interface MetaInf {
    String pack();

    String[] imports();

    String[] envImports();

    String[] dependencies();

    String className();

    String sourceCode();

    class Builder extends ImmutableMetaInf.Builder {
    }
}
