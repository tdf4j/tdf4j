package io.github.therealmone.tdf4j.generator.templates.adaptor;

import io.github.therealmone.tdf4j.model.ebnf.Repeat;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class RepeatAdaptor extends Prediction implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interp, final ST self, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final Repeat repeat = (Repeat) o;
        switch (propertyName) {
            case "start":
                return getStartElements(repeat.getElements()[0]);
            case "elements":
                return repeat.getElements();
            default:
                return null;
        }
    }

}
