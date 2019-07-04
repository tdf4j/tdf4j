package io.github.therealmone.tdf4j.generator.templates.adaptor;

import io.github.therealmone.tdf4j.model.ebnf.Or;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class OrAdaptor extends Prediction implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interp, final ST self, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final Or or = (Or) o;
        switch (propertyName) {
            case "firstStart":
                return getStartElements(or.getFirst());
            case "secondStart":
                return getStartElements(or.getSecond());
            case "first":
                return or.getFirst();
            case "second":
                return or.getSecond();
            default:
                return null;
        }
    }

}
