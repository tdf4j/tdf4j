package io.github.therealmone.tdf4j.processor.generators;

import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import io.github.therealmone.tdf4j.processor.Templates;
import org.stringtemplate.v4.ST;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.*;
import java.io.*;

public class ParserGenerator implements Generator {
    @Override
    public void generate(final ProcessingEnvironment processingEnv, final Element element) throws Exception {
        //todo: Вся логика генерации парсеров
        validate(processingEnv, element);
        final TypeElement clazz = (TypeElement) element;
        compile(clazz.getQualifiedName().toString());
        final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(clazz.getQualifiedName() + "Parser");
        final AbstractParserModule module = ((AbstractParserModule) Class.forName(clazz.toString()).newInstance()).build();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, module.getProductions().toString());
        try(final Writer writer = javaFileObject.openWriter()) {
            writer.write(
                    process(module,
                            clazz.getQualifiedName().toString().replaceAll("\\." + clazz.getSimpleName() + "$", ""),
                            clazz.getSimpleName() + "Parser"));
            writer.flush();
        }
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

    private String process(final AbstractParserModule module, final String pack, final String name) {
        final ST testST = Templates.TEST.template();
        System.out.println(pack);
        System.out.println(name);
        testST.add("package", pack);
        testST.add("className", name);
        return testST.render();
    }

    private void compile(final String name) throws Exception {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null,
                "parser-api/src/main/java/" + name.replaceAll("\\.", "/") + ".java");
    }
}
