/*
 * Copyright (c) 2019 tdf4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.tdf4j.generator.templates.renderer;

import io.github.tdf4j.generator.Template;
import io.github.tdf4j.core.model.ebnf.Element;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.STGroup;

import java.util.Locale;

public class ElementRenderer implements AttributeRenderer {
    @Override
    public String toString(final Object o, final String formatString, final Locale locale) {
        final Element element = (Element) o;
        final STGroup template = Template.JAVA.template();
        switch (element.kind()) {
            case GROUP:
                return template.getInstanceOf("ele_group").add("ele_group", element).render();

            case NON_TERMINAL:
                return template.getInstanceOf("non_terminal").add("non_terminal", element).render();

            case OPTIONAL:
                return template.getInstanceOf("optional").add("optional", element).render();

            case REPEAT:
                return template.getInstanceOf("repeat").add("repeat", element).render();

            case REPETITION:
                return template.getInstanceOf("repetition").add("repetition", element).render();

            case TERMINAL:
                return template.getInstanceOf("terminal").add("terminal", element).render();

            case INLINE_ACTION:
                return template.getInstanceOf("inline_action").add("inline_action", element).render();

            case OR:
                return template.getInstanceOf("or").add("or", element).render();

            case ALTERNATIVE:
                return this.toString(element.asAlternative().getElement(), formatString, locale);

            default:
                return null;
        }
    }
}
