package io.github.therealmone.tdf4j.validator.lexical;

import io.github.therealmone.tdf4j.model.ebnf.Terminal;
import io.github.therealmone.tdf4j.module.lexer.AbstractLexerModule;
import io.github.therealmone.tdf4j.validator.ValidationTest;
import io.github.therealmone.tdf4j.validator.Validator;
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
