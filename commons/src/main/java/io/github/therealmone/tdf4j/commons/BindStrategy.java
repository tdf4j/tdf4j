package io.github.therealmone.tdf4j.lexer.config.strategies;

public interface BindStrategy<K, B, R> {
    B bind(K key);

    R build();
}
