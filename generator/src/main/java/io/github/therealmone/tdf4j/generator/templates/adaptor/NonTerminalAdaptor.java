package io.github.therealmone.tdf4j.generator.templates.adaptor;

import io.github.therealmone.tdf4j.model.ebnf.NonTerminal;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class NonTerminalAdaptor implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interp, final ST self, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final NonTerminal nonTerminal = (NonTerminal) o;
        switch (propertyName) {
            case "value":
                return nonTerminal.getValue();
            default:
                return null;
        }
    }

}
