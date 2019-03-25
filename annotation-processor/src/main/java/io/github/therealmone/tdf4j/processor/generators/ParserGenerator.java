package io.github.therealmone.tdf4j.processor.generators;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class ParserGenerator implements Generator {
    @Override
    public void generate(final ProcessingEnvironment processingEnv, final Element element) throws Exception {
        //todo: Вся логика генерации парсеров
        validate(processingEnv, element);
        final JavaFileObject javaFileObject = processingEnv.getFiler().createClassFile(element.getSimpleName() + "Parser");
        final Class clazz = Class.forName(element.toString());
        final AbstractParserModule abstractParserModule = ((AbstractParserModule) clazz.newInstance()).build();
        System.out.println(abstractParserModule.getProductions());
    }

    private void validate(final ProcessingEnvironment processingEnv, final Element element) {
        if(element.getKind() != ElementKind.CLASS) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only classes can be annotated with @Parser");
        }

        final TypeElement typeElement = (TypeElement) element;
        if(!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Annotated with @Parser class must be public");
        }

        if(typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Annotated with @Parser class can't be abstract");
        }

        final TypeMirror superClass = processingEnv.getElementUtils().getTypeElement(AbstractParserModule.class.getCanonicalName()).asType();
        if(!typeElement.getSuperclass().toString().equalsIgnoreCase(superClass.toString())) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Annotated with @Parser class must extend AbstractParserModule");
        }
    }
}
