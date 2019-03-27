package io.github.therealmone.tdf4j.generator;

import io.github.therealmone.tdf4j.commons.Module;

public interface Generator<T> {
    T generate(final Module module);
}
