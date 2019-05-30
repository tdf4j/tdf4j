package io.github.therealmone.tdf4j.tdfparser.builder;

import io.github.therealmone.tdf4j.model.ast.ASTNode;
import io.github.therealmone.tdf4j.model.ebnf.Element;

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
        final ASTNode element = tree.children().get(0).asNode();
        switch (element.tag()) {
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
