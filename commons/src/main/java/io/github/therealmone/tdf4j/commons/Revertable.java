package io.github.therealmone.tdf4j.commons;

public interface Revertable {
    void revert(final Anchor anchor);

    Anchor setAnchor();
}
