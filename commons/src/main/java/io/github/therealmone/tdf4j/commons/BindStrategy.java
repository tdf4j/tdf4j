package io.github.therealmone.tdf4j.commons;

public interface BindStrategy<K, B, R> {
    B bind(K key);

    R build();
}
