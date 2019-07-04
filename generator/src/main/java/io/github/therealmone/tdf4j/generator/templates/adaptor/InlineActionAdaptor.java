package io.github.therealmone.tdf4j.generator.templates.adaptor;

import io.github.therealmone.tdf4j.model.ebnf.InlineAction;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class InlineActionAdaptor implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interp, final ST self, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final InlineAction inlineAction = (InlineAction) o;
        switch (propertyName) {
            case "code":
                return inlineAction.getCode();
            default:
                return null;
        }
    }

}
