/*
 * Copyright 2019 Roman Fatnev
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
package io.github.therealmone.tdf4j.generator;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public enum Template {
    PARSER("parser"),
    IMPORTS("imports"),
    METHOD("method"),
    LOGIC_TERMINAL("terminal_tag"),
    LOGIC_NON_TERMINAL("non_terminal"),
    LOGIC_OPTIONAL("optional"),
    LOGIC_OR("or"),
    LOGIC_REPEAT("repeat"),
    LOGIC_REPETITION("repetition"),
    LOGIC_GROUP("ele_group"),
    LOGIC_EXCEPT("except"),
    LOGIC_INLINE_ACTION("inline_action");

    private static final STGroup JAVA_TEMPLATE = new STGroupFile("templates/java.stg");

    static {
        JAVA_TEMPLATE.load();
    }

    private final String template;

    Template(final String template) {
        this.template = template;
    }

    public ST template() {
        return JAVA_TEMPLATE.getInstanceOf(template);
    }
}
