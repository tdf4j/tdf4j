package io.github.therealmone.tdf4j.processor;

import com.google.auto.service.AutoService;
import io.github.therealmone.tdf4j.processor.generators.Generator;
import io.github.therealmone.tdf4j.processor.generators.GeneratorFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes({"io.github.therealmone.tdf4j.parser.api.Parser"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {
    private final GeneratorFactory generatorFactory = new GeneratorFactory();
    private ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        for(final TypeElement annotation : annotations) {
            final Generator generator = generatorFactory.create(annotation);
            if(generator == null) {
                throw new RuntimeException("No generator for annotation: " + annotation.getQualifiedName());
            }
            for(final Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                try {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,"PARSER GENERATION: " + annotation + " ; " + element);
                    generator.generate(processingEnv, element);
                } catch (Exception e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
