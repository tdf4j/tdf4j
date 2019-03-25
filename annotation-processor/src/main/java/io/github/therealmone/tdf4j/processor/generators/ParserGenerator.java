package io.github.therealmone.tdf4j.processor.generators;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class ParserGenerator implements Generator {
    @Override
    public void generate(ProcessingEnvironment processingEnv, Element element) throws Exception {
        //todo: Вся логика генерации парсеров
        processingEnv.getFiler().createClassFile(element.getSimpleName() + "Parser");
    }
}
