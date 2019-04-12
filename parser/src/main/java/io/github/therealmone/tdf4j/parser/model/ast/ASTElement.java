package io.github.therealmone.tdf4j.parser.model.ast;

public interface ASTElement {

    Kind kind();

    default boolean isLeaf() {
        return this.kind() == Kind.LEAF;
    }

    default ASTLeaf asLeaf() {
        return (ASTLeaf) this;
    }

    default boolean isNode() {
        return this.kind() == Kind.NODE;
    }

    default ASTNode asNode() {
        return (ASTNode) this;
    }

    default boolean isRoot() {
        return this.kind() == Kind.ROOT;
    }

    default ASTRoot asRoot() {
        return (ASTRoot) this;
    }

    enum Kind {
        NODE,
        LEAF,
        ROOT
    }
}
