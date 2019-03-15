package io.github.therealmone.tdf4j.lexer.config;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.Rule;
import org.xml.sax.Attributes;

import java.io.StringReader;

public class XmlLexerModule extends AbstractLexerModule {
    private final String xml;
    private final Digester digester;

    public XmlLexerModule(final String xml) {
        this.xml = xml;
        this.digester = new Digester();
        digester.addRule("terminals/terminal", new Rule() {
            @Override
            public void begin(String namespace, String name, Attributes attributes) throws Exception {
                tokenize(attributes.getValue("tag"))
                        .pattern(attributes.getValue("pattern"))
                        .priority(attributes.getValue("priority") != null ? Long.parseLong(attributes.getValue("priority")) : 0);
            }
        });
    }

    @Override
    public void configure() {
        try {
            digester.parse(new StringReader(xml));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
