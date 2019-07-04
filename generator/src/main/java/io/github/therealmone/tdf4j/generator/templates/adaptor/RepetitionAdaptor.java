package io.github.therealmone.tdf4j.generator.templates.adaptor;

import io.github.therealmone.tdf4j.model.ebnf.Repetition;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class RepetitionAdaptor implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interp, final ST self, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final Repetition repetition = (Repetition) o;
        switch(propertyName) {
            case "hash":
                return repetition.hashCode();
            case "times":
                return repetition.getTimes();
            case "element":
                return repetition.getElement();
            default:
                return null;
        }
    }

}
