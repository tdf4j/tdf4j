package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;

public interface Builder<T> {

    T build(final ASTNode tree);

}
