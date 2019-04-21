package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Element;
import io.github.therealmone.tdf4j.generator.templates.Buildable;

import javax.annotation.Nullable;

public interface CodeBlock extends Buildable {

    @Nullable
    static CodeBlock fromElement(final Element element) {
        switch (element.kind()) {
            case TERMINAL_TAG:
                return new TerminalTagTemplate.Builder().terminalTag(element.asTerminalTag()).build();
            case NON_TERMINAL:
                return new NonTerminalTemplate.Builder().nonTerminal(element.asNonTerminal()).build();
            case OPTIONAL:
                return new OptionalTemplate.Builder().optional(element.asOptional()).build();
            case OR:
                return new OrTemplate.Builder().or(element.asOr()).build();
            case REPEAT:
                return new RepeatTemplate.Builder().repeat(element.asRepeat()).build();
            case REPETITION:
                return new RepetitionTemplate.Builder().repetition(element.asRepetition()).build();
            case GROUP:
                return new GroupTemplate.Builder().group(element.asGroup()).build();
            case EXCEPT:
                return new ExceptTemplate.Builder().except(element.asExcept()).build();
            case INLINE_ACTION:
                return new InlineActionTemplate.Builder().inline(element.asInlineAction()).build();
            default:
                return null;
        }
    }
}
