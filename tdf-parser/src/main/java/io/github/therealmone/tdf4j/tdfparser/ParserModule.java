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
package io.github.therealmone.tdf4j.tdfparser;

import io.github.therealmone.tdf4j.module.parser.AbstractParserModule;

public class ParserModule extends AbstractParserModule {

    @Override
    public void configure() {
        prod("tdf_lang")
                .is(
                        nt("lexis"),
                        optional(nt("environment")),
                        nt("syntax"),
                        t("EOF")
                );

        prod("lexis")
                .is(
                        t("KEY_LEXIS"),
                        repeat(
                                nt("terminal_description")
                        )
                );

        prod("terminal_description")
                .is(
                        t("TERMINAL_TAG"),
                        t("STRING"),
                        optional(nt("terminal_parameters"))
                );

        prod("terminal_parameters")
                .is(
                        t("LEFT_SQUARE_BRACKET"),
                        nt("terminal_parameters_values"),
                        t("RIGHT_SQUARE_BRACKET")
                );

        prod("terminal_parameters_values")
                .is(
                        oneOf(
                                nt("terminal_parameter_priority"),
                                nt("terminal_parameter_hidden"),
                                nt("terminal_parameter_pattern_flag")
                        ),
                        optional(t("COMMA"), nt("terminal_parameters_values"))
                );

        prod("terminal_parameter_priority")
                .is(
                        t("TERMINAL_PARAMETER_PRIORITY"),
                        t("COLON"),
                        t("INTEGER")
                );

        prod("terminal_parameter_hidden")
                .is(
                        t("TERMINAL_PARAMETER_HIDDEN"),
                        t("COLON"),
                        t("BOOLEAN")
                );

        prod("terminal_parameter_pattern_flag")
                .is(
                        t("TERMINAL_PARAMETER_PATTERN_FLAG"),
                        t("COLON"),
                        oneOf(
                                t("TERMINAL_PARAMETER_PATTERN_FLAG"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE"),
                                t("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ")
                        )
                );

        prod("environment")
                .is(
                        t("KEY_ENV"),
                        repeat(nt("env_import")),
                        optional(nt("env_code"))
                );

        prod("env_import")
                .is(
                        t("KEY_IMPORT"),
                        t("STRING")
                );

        prod("env_code")
                .is(
                        t("KEY_CODE"),
                        t("STRING")
                );

        prod("syntax")
                .is(
                        t("KEY_SYNTAX"),
                        repeat(nt("production_description"))
                );

        prod("production_description")
                .is(
                        t("NON_TERMINAL"),
                        t("OP_ASSIGN"),
                        optional(nt("ebnf_elements_set")),
                        t("DELIMITER")
                );

        prod("ebnf_elements_set")
                .is(
                        or(
                                nt("ebnf_element"),
                                nt("inline_action")
                        ),
                        optional(t("COMMA"), nt("ebnf_elements_set"))
                );

        prod("ebnf_element")
                .is(
                        oneOf(
                                nt("ebnf_optional"),
                                nt("ebnf_one_of"),
                                nt("ebnf_repeat"),
                                nt("ebnf_repetition"),
                                nt("ebnf_group"),
                                t("TERMINAL_TAG"),
                                t("NON_TERMINAL")
                        )
                );

        prod("ebnf_optional")
                .is(
                        t("LEFT_SQUARE_BRACKET"),
                        nt("ebnf_elements_set"),
                        t("RIGHT_SQUARE_BRACKET")
                );

        prod("ebnf_one_of")
                .is(
                        repeat(
                                t("LOP_OR"),
                                nt("ebnf_element")
                        )
                );

        prod("ebnf_repeat")
                .is(
                        t("LEFT_FIGURE_BRACKET"),
                        nt("ebnf_elements_set"),
                        t("RIGHT_FIGURE_BRACKET")
                );

        prod("ebnf_repetition")
                .is(
                        t("INTEGER"),
                        t("OP_MULTIPLY"),
                        nt("ebnf_element")
                );

        prod("ebnf_group")
                .is(
                        t("LEFT_BRACKET"),
                        nt("ebnf_elements_set"),
                        t("RIGHT_BRACKET")
                );

        prod("inline_action")
                .is(
                        t("LEFT_INLINE_ACTION_BRACKET"),
                        t("STRING"),
                        t("RIGHT_INLINE_ACTION_BRACKET")
                );
    }

}
