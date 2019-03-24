package io.github.therealmone.tdf4j.parser.processor.generators;

import javax.annotation.Nullable;
import javax.lang.model.element.TypeElement;

public class GeneratorFactory {
    @Nullable
    public Generator create(final TypeElement annotation) {
        if(annotation.getSimpleName().contentEquals("Parser")) {
            return new ParserGenerator();
        }

        return null;
    }
}
