/*
 * Copyright (c) 2019 Roman Fatnev
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
import org.tdf4j.model.ebnf.NonTerminal;
import org.tdf4j.tdfparser.processor.StringProcessor;

public class EbnfNonTerminalBuilder extends AbstractEbnfElementBuilder<NonTerminal> {
    private final StringProcessor stringProcessor = new StringProcessor();

    EbnfNonTerminalBuilder(BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public NonTerminal build(final ASTNode tree) {
        return new NonTerminal.Builder()
                .setValue(tree.getChildren().get(0).asLeaf().getToken().getValue())
                .setNodeAction(tree.getChildren().size() > 1
                        ? stringProcessor.process(tree.getChildren().get(2).asLeaf().getToken().getValue())
                        : null
                ).build();
    }

}
