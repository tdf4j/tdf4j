package io.github.therealmone.tdf4j.tdfparser.constructor;

import io.github.therealmone.tdf4j.model.ast.ASTElement;
import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.model.ebnf.Production;
import io.github.therealmone.tdf4j.tdfparser.builder.BuilderMapper;

import java.util.ArrayList;
import java.util.List;


public class ProductionConstructor implements Constructor {
    private final BuilderMapper ebnfElementBuilder = new BuilderMapper();
    private final Production.Builder builder;
    private ASTNode elementSet;

    public ProductionConstructor(final Production.Builder builder) {
        this.builder = builder;
    }

    @Override
    public void construct() {
        for(final ASTNode element : collectElements(elementSet)) {
            final Element builtElement = ebnfElementBuilder.build(element);
            if(builtElement != null) {
                builder.then(builtElement);
            }
        }
    }

    private List<ASTNode> collectElements(final ASTNode elementSet) {
        final List<ASTNode> elements = new ArrayList<>();
        for(final ASTElement child : elementSet.children()) {
            if(!child.isNode()) {
                continue;
            }
            if(child.asNode().tag().equalsIgnoreCase("ebnf_element")) {
                elements.add(child.asNode());
            } else if(child.asNode().tag().equalsIgnoreCase("ebnf_elements_set")) {
                elements.addAll(collectElements(child.asNode()));
            }
        }
        return elements;
    }

    public void setElements(final ASTNode elementSet) {
        this.elementSet = elementSet;
    }
}
