/*
 * Copyright (c) 2019 Roman Fatnev
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
package org.tdf4j.tdfparser;

import org.tdf4j.module.lexer.AbstractLexerModule;

import java.util.regex.Pattern;

public class TdfLexerModule extends AbstractLexerModule {

    @Override
    protected void configure() {
        //lexis
        tokenize("EOF").pattern("\\$");
        tokenize("KEY_LEXIS").pattern("lexis");
        tokenize("TERMINAL_TAG").pattern("[A-Z][A-Z0-9_]*");
        tokenize("STRING").pattern("\"((\\\\\")|[^\"])*\"");
        tokenize("LEFT_SQUARE_BRACKET").pattern("\\[");
        tokenize("RIGHT_SQUARE_BRACKET").pattern("\\]");
        tokenize("INTEGER").pattern("-?(0|([1-9][0-9]*))").priority(1);
        tokenize("COLON").pattern(":");
        tokenize("BOOLEAN").pattern("true|false", Pattern.CASE_INSENSITIVE).priority(1);
        tokenize("COMMA").pattern(",");

        //terminal parameters
        tokenize("TERMINAL_PARAMETER_PRIORITY").pattern("priority");
        tokenize("TERMINAL_PARAMETER_HIDDEN").pattern("hidden");
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG").pattern("pattern");
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES").pattern("UNIX_LINES").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE").pattern("CASE_INSENSITIVE").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS").pattern("COMMENTS").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE").pattern("MULTILINE").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL").pattern("LITERAL").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL").pattern("DOTALL").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE").pattern("UNICODE_CASE").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ").pattern("CANON_EQ").priority(1);
        tokenize("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS").pattern("UNICODE_CHARACTER_CLASS").priority(1);

        //environment
        tokenize("KEY_ENV").pattern("env");
        tokenize("KEY_IMPORT").pattern("import");
        tokenize("KEY_CODE").pattern("code");

        //syntax
        tokenize("KEY_SYNTAX").pattern("syntax");
        tokenize("DELIMITER").pattern(";");
        tokenize("NON_TERMINAL").pattern("[a-z][a-z0-9_]*").priority(-1);
        tokenize("LOP_OR").pattern("\\|");
        tokenize("LEFT_FIGURE_BRACKET").pattern("\\{");
        tokenize("RIGHT_FIGURE_BRACKET").pattern("\\}");
        tokenize("OP_MULTIPLY").pattern("\\*");
        tokenize("LEFT_BRACKET").pattern("\\(");
        tokenize("RIGHT_BRACKET").pattern("\\)");
        tokenize("LEFT_INLINE_ACTION_BRACKET").pattern("<");
        tokenize("RIGHT_INLINE_ACTION_BRACKET").pattern(">");
        tokenize("OP_ASSIGN").pattern("=");
        tokenize("OP_SUM").pattern("\\+");
        tokenize("LAMBDA").pattern("->");

        tokenize("WS").pattern("\\s|\\n|\\r").priority(Integer.MAX_VALUE).hidden(true);
        tokenize("SINGLE_LINE_COMMENT").pattern("//.*(\n|\r|\r\n|\n\r)").priority(Integer.MAX_VALUE).hidden(true);
        tokenize("MULTI_LINE_COMMENT").pattern("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", Pattern.MULTILINE + Pattern.DOTALL)
                .priority(Integer.MAX_VALUE).hidden(true);
    }

}
