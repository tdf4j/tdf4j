/*
 *
 *  Copyright (c) 2019 tdf4j
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.tdf4j.generator.templates.adaptor;

import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;
import io.github.tdf4j.core.model.ebnf.Alternative;

import static io.github.tdf4j.core.model.ebnf.Elements.*;

public class AlternativeAdaptor implements ModelAdaptor {

    @Override
    public Object getProperty(final Interpreter interpreter, final ST st, final Object o, final Object property, final String propertyName) throws STNoSuchPropertyException {
        final Alternative alt = (Alternative) o;
        switch (propertyName) {
            case "element" : return alt.getElement();
            case "start" : return getStartElements(alt);
            case "index" : return alt.getIndex();
            default : throw new STNoSuchPropertyException(null, alt, propertyName);
        }
    }

}
