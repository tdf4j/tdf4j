package io.github.therealmone.tdf4j.commons;

import org.immutables.value.Value;

@Value.Immutable
public interface Environment {

    @Value.Default
    default String[] packages() {
        return new String[]{};
    }

    default String[] getPackages() {
        return packages();
    }

    @Value.Default
    default Dependency[] dependencies() {
        return new Dependency[]{};
    }

    default Dependency[] getDependencies() {
        return dependencies();
    }

    @Value.Default
    default String code() {
        return "";
    }

    default String getCode() {
        return code();
    }

    class Builder extends ImmutableEnvironment.Builder {
    }
}
