package io.github.therealmone.tdf4j.commons;

public interface Revertable<T> {
    T revert(final Anchor anchor);

    Anchor setAnchor();
}
