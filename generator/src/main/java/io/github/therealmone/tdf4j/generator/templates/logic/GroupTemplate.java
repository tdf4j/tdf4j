package io.github.therealmone.tdf4j.generator.templates.logic;

import io.github.therealmone.tdf4j.commons.model.ebnf.Element;
import io.github.therealmone.tdf4j.commons.model.ebnf.Group;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

@Value.Immutable
public interface GroupTemplate extends CodeBlock {

    Group group();

    @Override
    default String build() {
        final ST template = Template.LOGIC_GROUP.template()
                .add("comment", group().toString());
        for(final Element element : group().elements()) {
            final CodeBlock codeBlock = CodeBlock.fromElement(element);
            if(codeBlock != null) {
                template.add("codeBlocks", codeBlock.build());
            }
        }
        return template.render();
    }

    class Builder extends ImmutableGroupTemplate.Builder {
    }
}
