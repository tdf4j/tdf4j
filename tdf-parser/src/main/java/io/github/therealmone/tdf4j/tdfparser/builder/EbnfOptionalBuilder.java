package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.model.ebnf.Optional;


public class EbnfOptionalBuilder extends AbstractEbnfElementBuilder<Optional> {

    EbnfOptionalBuilder(BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Optional build(final ASTNode tree) {
        return new Optional.Builder()
                .elements(getInnerElements(tree).stream().map(this::callBuilder).toArray(Element[]::new))
                .build();
    }

}
