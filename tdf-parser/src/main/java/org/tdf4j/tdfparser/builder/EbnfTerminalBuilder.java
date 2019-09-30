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

import org.tdf4j.core.model.ast.ASTNode;
import org.tdf4j.core.model.ebnf.EBNFBuilder;
import org.tdf4j.core.model.ebnf.Terminal;
import org.tdf4j.tdfparser.processor.StringProcessor;

public class EbnfTerminalBuilder extends AbstractEbnfElementBuilder<Terminal> {
    private final StringProcessor stringProcessor = new StringProcessor();

    EbnfTerminalBuilder(final BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Terminal build(final ASTNode tree) {
        return EBNFBuilder.terminal(
                tree.getChildren().get(0).asLeaf().getToken().getValue(),
                tree.getChildren().size() > 1
                        ? stringProcessor.process(tree.getChildren().get(2).asLeaf().getToken().getValue())
                        : null);
    }

}
