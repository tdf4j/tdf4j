package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Terminal;

public class EbnfTerminalBuilder extends AbstractEbnfElementBuilder<Terminal.Tag> {

    EbnfTerminalBuilder(final BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Terminal.Tag build(final ASTNode tree) {
        return new Terminal.Tag.Builder()
                .value(tree.children().get(0).asLeaf().token().value())
                .build();
    }

}
