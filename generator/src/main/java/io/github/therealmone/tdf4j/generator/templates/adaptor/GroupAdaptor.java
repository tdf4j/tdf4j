package io.github.therealmone.tdf4j.generator.templates.adaptor;

import io.github.therealmone.tdf4j.model.ebnf.Group;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class GroupAdaptor implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interp, final ST self, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final Group group = (Group) o;
        switch (propertyName) {
            case "elements":
                return group.getElements();
            default:
                return null;
        }
    }

}
