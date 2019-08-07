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

package org.tdf4j.validator.lexical;

import org.tdf4j.model.ebnf.Terminal;
import org.tdf4j.module.lexer.AbstractLexerModule;
import org.tdf4j.validator.ValidationTest;
import org.tdf4j.validator.Validator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LexicalValidationTest extends ValidationTest {
    private final Validator<AbstractLexerModule> validator = Validator.lexical();

    @Test
    public void terminal_list_null() {
        assertThrows(
                validator,
                new AbstractLexerModule() {
                    @Override
                    public void configure() {
                    }

                    @Override
                    public List<Terminal> getTerminals() {
                        return null;
                    }
                },
                LexerValidatorException.LEXER_TERMINALS_NULL
        );
    }

    @Test
    public void terminal_tag_null() {
        assertThrows(
                validator,
                new AbstractLexerModule() {
                    @Override
                    public void configure() {
                    }

                    @Override
                    public List<Terminal> getTerminals() {
                        return new ArrayList<>() {{
                            add(new Terminal() {
                                @Override
                                public Tag getTag() {
                                    return null;
                                }

                                @Override
                                public Pattern getPattern() {
                                    return Pattern.compile("pattern");
                                }
                            });
                        }};
                    }
                },
                LexerValidatorException.LEXER_TERMINAL_TAG_NULL
        );
    }

    @Test
    public void terminal_tag_value_null() {
        assertThrows(
                validator,
                new AbstractLexerModule() {
                    @Override
                    public void configure() {
                    }

                    @Override
                    public List<Terminal> getTerminals() {
                        return new ArrayList<>() {{
                            add(new Terminal() {
                                @Override
                                public Tag getTag() {
                                    return new Tag() {
                                        @Override
                                        public String getValue() {
                                            return null;
                                        }
                                    };
                                }

                                @Override
                                public Pattern getPattern() {
                                    return Pattern.compile("pattern");
                                }
                            });
                        }};
                    }
                },
                LexerValidatorException.LEXER_TERMINAL_TAG_VALUE_NULL
        );
    }

    @Test
    public void terminal_pattern_null() {
        assertThrows(
                validator,
                new AbstractLexerModule() {
                    @Override
                    public void configure() {
                    }

                    @Override
                    public List<Terminal> getTerminals() {
                        return new ArrayList<>() {{
                            add(new Terminal() {
                                @Override
                                public Tag getTag() {
                                    return new Tag() {
                                        @Override
                                        public String getValue() {
                                            return "TAG";
                                        }
                                    };
                                }

                                @Override
                                public Pattern getPattern() {
                                    return null;
                                }
                            });
                        }};
                    }
                },
                LexerValidatorException.LEXER_TERMINAL_PATTERN_NULL
        );
    }

    @Test
    public void terminal_tag_collision() {
        assertThrows(
                validator,
                new AbstractLexerModule() {
                    @Override
                    public void configure() {
                    }

                    @Override
                    public List<Terminal> getTerminals() {
                        return new ArrayList<>() {{
                            add(new Terminal() {
                                @Override
                                public Tag getTag() {
                                    return new Tag() {
                                        @Override
                                        public String getValue() {
                                            return "TAG1";
                                        }
                                    };
                                }

                                @Override
                                public Pattern getPattern() {
                                    return Pattern.compile("pattern");
                                }
                            });

                            add(new Terminal() {
                                @Override
                                public Tag getTag() {
                                    return new Tag() {
                                        @Override
                                        public String getValue() {
                                            return "TAG1";
                                        }
                                    };
                                }

                                @Override
                                public Pattern getPattern() {
                                    return Pattern.compile("pattern");
                                }
                            });
                        }};
                    }
                },
                LexerValidatorException.LEXER_TERMINAL_COLLISION
        );
    }

}
