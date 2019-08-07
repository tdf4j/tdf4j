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
package org.tdf4j.tdfparser.builder;

import org.tdf4j.model.ast.ASTNode;
import org.tdf4j.model.ebnf.Or;

import java.util.List;

public class EbnfOrBuilder extends AbstractEbnfElementBuilder<Or> {

    EbnfOrBuilder(BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Or build(final ASTNode tree) {
        return oneOf(getInnerElements(tree));
    }

    private Or oneOf(final List<ASTNode> elements) {
        if(elements.size() == 2) {
            return new Or.Builder().setFirst(callBuilder(elements.get(0))).setSecond(callBuilder(elements.get(1))).build();
        } else {
            return new Or.Builder().setFirst(callBuilder(elements.get(0))).setSecond(oneOf(elements.subList(1, elements.size()))).build();
        }
    }

}
