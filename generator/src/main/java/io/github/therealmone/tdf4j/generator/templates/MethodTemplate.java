package io.github.therealmone.tdf4j.generator.templates;

import io.github.therealmone.tdf4j.generator.Template;
import io.github.therealmone.tdf4j.generator.templates.logic.CodeBlock;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

import java.util.List;

@Value.Immutable
public interface MethodTemplate extends Buildable {

    String name();

    @Value.Default
    default String returnValue() {
        return "void";
    }

    List<CodeBlock> codeBlocks();

    @Override
    default String build() {
        final ST template = Template.METHOD.template()
                .add("name", name())
                .add("returnValue", returnValue());
        codeBlocks().forEach(codeBlock -> template.add("codeBlocks", codeBlock.build()));
        return template.render();
    }

    class Builder extends ImmutableMethodTemplate.Builder {
    }
}
