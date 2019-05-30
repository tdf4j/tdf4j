package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.NonTerminal;

public class EbnfNonTerminalBuilder extends AbstractEbnfElementBuilder<NonTerminal> {

    EbnfNonTerminalBuilder(BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public NonTerminal build(final ASTNode tree) {
        return new NonTerminal.Builder()
                .identifier(tree.children().get(0).asLeaf().token().value())
                .build();
    }

}
