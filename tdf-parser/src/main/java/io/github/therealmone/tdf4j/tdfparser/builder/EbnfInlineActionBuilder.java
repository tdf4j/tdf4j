package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.InlineAction;
import io.github.therealmone.tdf4j.tdfparser.processor.StringProcessor;

public class EbnfInlineActionBuilder extends  AbstractEbnfElementBuilder<InlineAction> {
    private final StringProcessor stringProcessor = new StringProcessor();

    EbnfInlineActionBuilder(final BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public InlineAction build(final ASTNode tree) {
        return new InlineAction.Builder()
                .code(stringProcessor.process(tree.children().get(1).asLeaf().token().value()))
                .build();
    }

}
