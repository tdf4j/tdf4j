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
package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import io.github.therealmone.tdf4j.tdfparser.processor.StringProcessor;

public class EbnfTerminalBuilder extends AbstractEbnfElementBuilder<Terminal.Tag> {
    private final StringProcessor stringProcessor = new StringProcessor();

    EbnfTerminalBuilder(final BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Terminal.Tag build(final ASTNode tree) {
        return new Terminal.Tag.Builder()
                .setValue(tree.getChildren().get(0).asLeaf().getToken().getValue())
                .setTokenAction(tree.getChildren().size() > 1
                        ? stringProcessor.process(tree.getChildren().get(2).asLeaf().getToken().getValue())
                        : null
                ).build();
    }

}
