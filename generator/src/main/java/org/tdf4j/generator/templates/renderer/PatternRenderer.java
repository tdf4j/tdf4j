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

package org.tdf4j.generator.templates.renderer;

import org.apache.commons.text.StringEscapeUtils;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;
import java.util.regex.Pattern;

public class PatternRenderer implements AttributeRenderer {

    @Override
    public String toString(final Object o, final String formatString, final Locale locale) {
        final Pattern pattern = (Pattern) o;
        return StringEscapeUtils.escapeJava(pattern.pattern());
    }

}
