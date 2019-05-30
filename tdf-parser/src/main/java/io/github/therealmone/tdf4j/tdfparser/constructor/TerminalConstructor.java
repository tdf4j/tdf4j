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
package io.github.therealmone.tdf4j.tdfparser.constructor;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class TerminalConstructor implements Constructor {
    private final Terminal.Builder builder;
    private String pattern;
    private String flag;
    private String hidden;
    private String priority;

    public TerminalConstructor(final Terminal.Builder builder) {
        this.builder = builder;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public void setPriority(String priority) {
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
            builder.pattern(compilePattern(pattern, flag));
        }
    }

    private boolean isNotBlankOrNull(@Nullable final String s) {
        return s != null && !s.trim().equalsIgnoreCase("");
    }

    private Pattern compilePattern(final String pattern, @Nullable final String flag) {
        if(flag == null) {
            return Pattern.compile(pattern);
        }
        switch (flag) {
            case "UNIX_LINES":
                return Pattern.compile(pattern, Pattern.UNIX_LINES);
            case "CASE_INSENSITIVE":
                return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            case "COMMENTS":
                return Pattern.compile(pattern, Pattern.COMMENTS);
            case "MULTILINE":
                return Pattern.compile(pattern, Pattern.MULTILINE);
            case "LITERAL":
                return Pattern.compile(pattern, Pattern.LITERAL);
            case "DOTALL":
                return Pattern.compile(pattern, Pattern.DOTALL);
            case "UNICODE_CASE":
                return Pattern.compile(pattern, Pattern.UNICODE_CASE);
            case "CANON_EQ":
                return Pattern.compile(pattern, Pattern.CANON_EQ);
            case "UNICODE_CHARACTER_CLASS":
                return Pattern.compile(pattern, Pattern.UNICODE_CHARACTER_CLASS);
            default:
                return Pattern.compile(pattern);
        }
    }
}
