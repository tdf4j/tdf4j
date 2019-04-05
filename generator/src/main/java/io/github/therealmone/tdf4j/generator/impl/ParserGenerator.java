package io.github.therealmone.tdf4j.generator.impl;

import io.github.therealmone.tdf4j.commons.Module;
import io.github.therealmone.tdf4j.commons.model.ebnf.Grammar;
import io.github.therealmone.tdf4j.commons.model.ebnf.Production;
import io.github.therealmone.tdf4j.generator.Generator;
import io.github.therealmone.tdf4j.generator.Template;
import io.github.therealmone.tdf4j.generator.templates.ImmutableMethodTemplate;
import io.github.therealmone.tdf4j.generator.templates.MethodTemplate;
import io.github.therealmone.tdf4j.generator.templates.ParserTemplate;
import io.github.therealmone.tdf4j.generator.templates.logic.CodeBlock;
import io.github.therealmone.tdf4j.parser.Parser;
import io.github.therealmone.tdf4j.parser.config.AbstractParserModule;
import org.joor.Reflect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ParserGenerator implements Generator<Parser> {

    @Override
    public Parser generate(Module module) {
        if(!(module instanceof AbstractParserModule)) {
            throw new RuntimeException("Parser can be generated only from AbstractParserModule");
        }
        return process(((AbstractParserModule) module).build());
    }

    private Parser process(final AbstractParserModule module) {
        final String generatedClassName = "AutoGeneratedParserFrom_" + module.getClass().getName().replaceFirst(module.getClass().getPackage().getName() + ".", "");
        final ParserTemplate parser = build(module.getGrammar(), generatedClassName, module.getClass().getPackage().getName());
        System.out.println(parser.build());
        return Reflect.compile(module.getClass().getPackage().getName() + "." + generatedClassName,
                parser.build()
        ).create().get();
    }

    private ParserTemplate build(final Grammar grammar, final String className, final String pack) {
        final ParserTemplate.Builder parserBuilder = new ParserTemplate.Builder()
                .className(className)
                .pack(pack)
                .imports(Template.IMPORTS.template().render());
        if(grammar.initProduction() == null) {
            throw new RuntimeException("Initial production is null");
        }
        //noinspection ConstantConditions
        parserBuilder.initProd(grammar.initProduction());
        parserBuilder.addAllMethods(collectMethods(grammar.productions()));
        return parserBuilder.build();
    }

    private List<MethodTemplate> collectMethods(final List<Production> productions) {
        final Map<String, MethodTemplate.Builder> declaredMethods = new HashMap<>();
        for (final Production production : productions) {
            if(!declaredMethods.containsKey(production.identifier())) {
                declaredMethods.put(production.identifier(), new MethodTemplate.Builder()
                        .name(production.identifier())
                        .comment(production.toString())
                );
            }

            final MethodTemplate.Builder builder = declaredMethods.get(production.identifier());
            production.elements().forEach(element -> {
                final CodeBlock codeBlock = CodeBlock.fromElement(element);
                if(codeBlock != null) {
                    builder.addCodeBlocks(codeBlock);
                }
            });
        }
        return declaredMethods.values().stream().map((Function<MethodTemplate.Builder, MethodTemplate>) ImmutableMethodTemplate.Builder::build).collect(Collectors.toList());
    }
}
