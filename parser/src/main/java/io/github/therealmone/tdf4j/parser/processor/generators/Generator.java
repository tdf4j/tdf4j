package io.github.therealmone.tdf4j.parser.processor.generators;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public interface Generator {
    void generate(final ProcessingEnvironment processingEnv, final Element element) throws Exception;
}
