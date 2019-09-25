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
import org.tdf4j.core.model.ebnf.Element;

import javax.annotation.Nullable;

public class BuilderMapper implements Builder<Element>  {
    private final EbnfGroupBuilder groupBuilder = new EbnfGroupBuilder(this);
    private final EbnfOptionalBuilder optionalBuilder = new EbnfOptionalBuilder(this);
    private final EbnfTerminalBuilder terminalBuilder = new EbnfTerminalBuilder(this);
    private final EbnfInlineActionBuilder inlineActionBuilder = new EbnfInlineActionBuilder(this);
    private final EbnfNonTerminalBuilder nonTerminalBuilder = new EbnfNonTerminalBuilder(this);
    private final EbnfOrBuilder orBuilder = new EbnfOrBuilder(this);
    private final EbnfRepeatBuilder repeatBuilder = new EbnfRepeatBuilder(this);
    private final EbnfRepetitionBuilder repetitionBuilder = new EbnfRepetitionBuilder(this);

    @Override
    @Nullable
    public Element build(final ASTNode tree) {
        final ASTNode element = tree.getChildren().get(0).asNode();
        switch (element.getTag()) {
            case "ebnf_optional":
                return optionalBuilder.build(element);
            case "ebnf_or":
                return orBuilder.build(element);
            case "ebnf_repeat":
                return repeatBuilder.build(element);
            case "ebnf_repetition":
                return repetitionBuilder.build(element);
            case "ebnf_group":
                return groupBuilder.build(element);
            case "ebnf_terminal":
                return terminalBuilder.build(element);
            case "ebnf_non_terminal":
                return nonTerminalBuilder.build(element);
            case "ebnf_inline_action":
                return inlineActionBuilder.build(element);
            default:
                return null;
        }
    }

}
