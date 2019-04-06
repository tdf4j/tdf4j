package io.github.therealmone.tdf4j.generator.templates;

import io.github.therealmone.tdf4j.commons.Environment;
import io.github.therealmone.tdf4j.generator.Template;
import org.immutables.value.Value;
import org.stringtemplate.v4.ST;

import java.util.List;

@Value.Immutable
public interface ParserTemplate extends Buildable {

    String pack();

    String imports();

    String className();

    Environment environment();

    String initProd();

    List<MethodTemplate> methods();

    @Override
    default String build() {
        final ST template = Template.PARSER.template()
                .add("package", pack())
                .add("imports", imports())
                .add("className", className())
                .add("environment", environment())
                .add("initProd", initProd());
        methods().forEach(method -> template.add("methods", method.build()));
        return template.render();
    }

    class Builder extends ImmutableParserTemplate.Builder {
    }
}
