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
package org.tdf4j.tdfparser.constructor;

import org.tdf4j.core.model.ebnf.Terminal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TerminalConstructor implements Constructor {
    private final Terminal.Builder builder;
    private String pattern;
    private List<String> flags = new ArrayList<>();
    private String hidden;
    private String priority;

    public TerminalConstructor(final Terminal.Builder builder) {
        this.builder = builder;
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    public void addFlag(final String flag) {
        this.flags.add(flag);
    }

    public void setHidden(final String hidden) {
        this.hidden = hidden;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    @Override
    public void construct() {
        if(isNotBlankOrNull(priority)) {
            builder.priority(Long.parseLong(priority));
        }
        if(isNotBlankOrNull(hidden)) {
            builder.hidden(Boolean.parseBoolean(hidden));
        }
        if(isNotBlankOrNull(pattern)) {
            builder.setPattern(compilePattern(pattern, flags));
        }
    }

    private boolean isNotBlankOrNull(@Nullable final String s) {
        return s != null && !s.trim().equalsIgnoreCase("");
    }

    private Pattern compilePattern(final String pattern, @Nonnull final List<String> flags) {
        if(flags.isEmpty()) {
            return Pattern.compile(pattern);
        }
        int compilationFlag = 0;
        for(final String flag : flags) {
            switch (flag) {
                case "UNIX_LINES":
                    compilationFlag += Pattern.UNIX_LINES;
                    break;
                case "CASE_INSENSITIVE":
                    compilationFlag += Pattern.CASE_INSENSITIVE;
                    break;
                case "COMMENTS":
                    compilationFlag += Pattern.COMMENTS;
                    break;
                case "MULTILINE":
                    compilationFlag += Pattern.MULTILINE;
                    break;
                case "LITERAL":
                    compilationFlag += Pattern.LITERAL;
                    break;
                case "DOTALL":
                    compilationFlag += Pattern.DOTALL;
                    break;
                case "UNICODE_CASE":
                    compilationFlag += Pattern.UNICODE_CASE;
                    break;
                case "CANON_EQ":
                    compilationFlag += Pattern.CANON_EQ;
                    break;
                case "UNICODE_CHARACTER_CLASS":
                    compilationFlag += Pattern.UNICODE_CHARACTER_CLASS;
                    break;
            }
        }
        return Pattern.compile(pattern, compilationFlag);
    }
}
