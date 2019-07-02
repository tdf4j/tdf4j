/*
 * Copyright 2019 Roman Fatnev
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
import io.github.therealmone.tdf4j.model.ebnf.Element;
import io.github.therealmone.tdf4j.model.ebnf.Group;

public class EbnfGroupBuilder extends AbstractEbnfElementBuilder<Group> {

    EbnfGroupBuilder(final BuilderMapper mapper) {
        super(mapper);
    }

    @Override
    public Group build(final ASTNode tree) {
        return new Group.Builder()
                .setElements(getInnerElements(tree).stream().map(this::callBuilder).toArray(Element[]::new))
                .build();
    }
}
