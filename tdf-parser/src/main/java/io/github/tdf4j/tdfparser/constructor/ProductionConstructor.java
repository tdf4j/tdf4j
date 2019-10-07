/*
 * Copyright (c) 2019 tdf4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.tdf4j.tdfparser.constructor;

import io.github.tdf4j.core.model.ast.ASTElement;
import io.github.tdf4j.core.model.ast.ASTNode;
import io.github.tdf4j.core.model.ebnf.Element;
import io.github.tdf4j.core.model.Production;
import io.github.tdf4j.tdfparser.builder.BuilderMapper;

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
        for(final ASTElement child : elementSet.getChildren()) {
            if(!child.isNode()) {
                continue;
            }
            if(child.asNode().getTag().equalsIgnoreCase("ebnf_element")) {
                elements.add(child.asNode());
            } else if(child.asNode().getTag().equalsIgnoreCase("ebnf_elements_set")) {
                elements.addAll(collectElements(child.asNode()));
            }
        }
        return elements;
    }

    public void setElements(final ASTNode elementSet) {
        this.elementSet = elementSet;
    }
}
