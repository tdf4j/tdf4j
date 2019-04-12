package io.github.therealmone.tdf4j.parser.model.ast;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Modifiable
public abstract class ASTNode implements ASTElement {

    @Override
    public Kind kind() {
        return Kind.NODE;
    }

    public abstract ASTElement parent();

    public abstract List<ASTElement> children();

    public abstract String tag();

    @Override
    public String toString() {
        return this.getClass().getName() + this.hashCode();
    }
}
