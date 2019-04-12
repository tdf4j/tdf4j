package io.github.therealmone.tdf4j.parser.model.ast;

import io.github.therealmone.tdf4j.commons.Token;
import org.immutables.value.Value;

@Value.Immutable
@Value.Modifiable
public abstract class ASTLeaf implements ASTElement{

    @Override
    public Kind kind() {
        return Kind.LEAF;
    }

    public abstract ASTElement parent();

    public abstract Token token();

    @Override
    public String toString() {
        return token().toString();
    }
}
