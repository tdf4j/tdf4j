package io.github.therealmone.tdf4j.generator.templates.adaptor;

import io.github.therealmone.tdf4j.model.ebnf.Optional;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class OptionalAdaptor extends Prediction implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interp, final ST self, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final Optional optional = (Optional) o;
        switch (propertyName) {
            case "start":
                return getStartElements(optional.getElements()[0]);
            case "elements":
                return optional.getElements();
            default:
                return null;
        }
    }

}
