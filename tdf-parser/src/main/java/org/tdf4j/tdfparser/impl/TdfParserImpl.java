package org.tdf4j.tdfparser.impl;

import org.tdf4j.parser.*;
import org.tdf4j.lexer.*;
import org.tdf4j.core.module.*;
import org.tdf4j.core.model.*;
import org.tdf4j.core.model.ast.*;
import org.tdf4j.core.utils.*;
import java.util.*;
import java.util.function.*;
import org.tdf4j.core.model.ebnf.*;
import static org.tdf4j.core.model.ebnf.EBNFBuilder.*;
import static org.tdf4j.core.model.ast.ASTCursor.Movement.*;
import org.tdf4j.tdfparser.TdfParser;
import org.tdf4j.core.module.LexerAbstractModule;
import org.tdf4j.core.module.ParserAbstractModule;
import org.tdf4j.tdfparser.constructor.*;
import org.tdf4j.tdfparser.processor.*;

public class TdfParserImpl extends AbstractParser implements TdfParser {

    //Terminals
    private final Terminal EOF = terminal("EOF");
    private final Terminal KEY_LEXIS = terminal("KEY_LEXIS");
    private final Terminal TERMINAL_TAG = terminal("TERMINAL_TAG");
    private final Terminal STRING = terminal("STRING");
    private final Terminal LEFT_SQUARE_BRACKET = terminal("LEFT_SQUARE_BRACKET");
    private final Terminal RIGHT_SQUARE_BRACKET = terminal("RIGHT_SQUARE_BRACKET");
    private final Terminal INTEGER = terminal("INTEGER");
    private final Terminal COLON = terminal("COLON");
    private final Terminal BOOLEAN = terminal("BOOLEAN");
    private final Terminal COMMA = terminal("COMMA");
    private final Terminal TERMINAL_PARAMETER_PRIORITY = terminal("TERMINAL_PARAMETER_PRIORITY");
    private final Terminal TERMINAL_PARAMETER_HIDDEN = terminal("TERMINAL_PARAMETER_HIDDEN");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG = terminal("TERMINAL_PARAMETER_PATTERN_FLAG");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ");
    private final Terminal TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS = terminal("TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS");
    private final Terminal KEY_ENV = terminal("KEY_ENV");
    private final Terminal KEY_IMPORT = terminal("KEY_IMPORT");
    private final Terminal KEY_CODE = terminal("KEY_CODE");
    private final Terminal KEY_SYNTAX = terminal("KEY_SYNTAX");
    private final Terminal DELIMITER = terminal("DELIMITER");
    private final Terminal NON_TERMINAL = terminal("NON_TERMINAL");
    private final Terminal LOP_OR = terminal("LOP_OR");
    private final Terminal LEFT_FIGURE_BRACKET = terminal("LEFT_FIGURE_BRACKET");
    private final Terminal RIGHT_FIGURE_BRACKET = terminal("RIGHT_FIGURE_BRACKET");
    private final Terminal OP_MULTIPLY = terminal("OP_MULTIPLY");
    private final Terminal LEFT_BRACKET = terminal("LEFT_BRACKET");
    private final Terminal RIGHT_BRACKET = terminal("RIGHT_BRACKET");
    private final Terminal LEFT_INLINE_ACTION_BRACKET = terminal("LEFT_INLINE_ACTION_BRACKET");
    private final Terminal RIGHT_INLINE_ACTION_BRACKET = terminal("RIGHT_INLINE_ACTION_BRACKET");
    private final Terminal OP_ASSIGN = terminal("OP_ASSIGN");
    private final Terminal OP_SUM = terminal("OP_SUM");
    private final Terminal LAMBDA = terminal("LAMBDA");
    private final Terminal WS = terminal("WS");
    private final Terminal SINGLE_LINE_COMMENT = terminal("SINGLE_LINE_COMMENT");
    private final Terminal MULTI_LINE_COMMENT = terminal("MULTI_LINE_COMMENT");

    //NonTerminals
    private final CallableNonTerminal ebnf_optional = callableNonTerminal("ebnf_optional", this::ebnf_optional);
    private final CallableNonTerminal ebnf_elements_set = callableNonTerminal("ebnf_elements_set", this::ebnf_elements_set);
    private final CallableNonTerminal ebnf_or = callableNonTerminal("ebnf_or", this::ebnf_or);
    private final CallableNonTerminal tdf_lang = callableNonTerminal("tdf_lang", this::tdf_lang);
    private final CallableNonTerminal lexis = callableNonTerminal("lexis", this::lexis);
    private final CallableNonTerminal env_import = callableNonTerminal("env_import", this::env_import);
    private final CallableNonTerminal terminal_parameter_pattern_flag = callableNonTerminal("terminal_parameter_pattern_flag", this::terminal_parameter_pattern_flag);
    private final CallableNonTerminal terminal_parameter_priority = callableNonTerminal("terminal_parameter_priority", this::terminal_parameter_priority);
    private final CallableNonTerminal production_description = callableNonTerminal("production_description", this::production_description);
    private final CallableNonTerminal ebnf_terminal = callableNonTerminal("ebnf_terminal", this::ebnf_terminal);
    private final CallableNonTerminal pattern_flags = callableNonTerminal("pattern_flags", this::pattern_flags);
    private final CallableNonTerminal environment = callableNonTerminal("environment", this::environment);
    private final CallableNonTerminal ebnf_non_terminal = callableNonTerminal("ebnf_non_terminal", this::ebnf_non_terminal);
    private final CallableNonTerminal terminal_parameters_values = callableNonTerminal("terminal_parameters_values", this::terminal_parameters_values);
    private final CallableNonTerminal ebnf_repetition = callableNonTerminal("ebnf_repetition", this::ebnf_repetition);
    private final CallableNonTerminal ebnf_group = callableNonTerminal("ebnf_group", this::ebnf_group);
    private final CallableNonTerminal terminal_parameters = callableNonTerminal("terminal_parameters", this::terminal_parameters);
    private final CallableNonTerminal env_code = callableNonTerminal("env_code", this::env_code);
    private final CallableNonTerminal ebnf_repeat = callableNonTerminal("ebnf_repeat", this::ebnf_repeat);
    private final CallableNonTerminal syntax = callableNonTerminal("syntax", this::syntax);
    private final CallableNonTerminal ebnf_element = callableNonTerminal("ebnf_element", this::ebnf_element);
    private final CallableNonTerminal ebnf_inline_action = callableNonTerminal("ebnf_inline_action", this::ebnf_inline_action);
    private final CallableNonTerminal terminal_parameter_hidden = callableNonTerminal("terminal_parameter_hidden", this::terminal_parameter_hidden);
    private final CallableNonTerminal terminal_description = callableNonTerminal("terminal_description", this::terminal_description);

    private final Lexer lexer = Lexer.get(new LexerAbstractModule() {
             @Override
             public void configure() {
                 tokenize(EOF).pattern("\\$").priority(0).hidden(false);
                 tokenize(KEY_LEXIS).pattern("lexis").priority(0).hidden(false);
                 tokenize(TERMINAL_TAG).pattern("[A-Z][A-Z0-9_]*").priority(0).hidden(false);
                 tokenize(STRING).pattern("\"((\\\\\")|[^\"])*\"").priority(0).hidden(false);
                 tokenize(LEFT_SQUARE_BRACKET).pattern("\\[").priority(0).hidden(false);
                 tokenize(RIGHT_SQUARE_BRACKET).pattern("\\]").priority(0).hidden(false);
                 tokenize(INTEGER).pattern("-?(0|([1-9][0-9]*))").priority(1).hidden(false);
                 tokenize(COLON).pattern(":").priority(0).hidden(false);
                 tokenize(BOOLEAN).pattern("true|false").priority(1).hidden(false);
                 tokenize(COMMA).pattern(",").priority(0).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PRIORITY).pattern("priority").priority(0).hidden(false);
                 tokenize(TERMINAL_PARAMETER_HIDDEN).pattern("hidden").priority(0).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG).pattern("pattern").priority(0).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES).pattern("UNIX_LINES").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE).pattern("CASE_INSENSITIVE").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS).pattern("COMMENTS").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE).pattern("MULTILINE").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL).pattern("LITERAL").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL).pattern("DOTALL").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE).pattern("UNICODE_CASE").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ).pattern("CANON_EQ").priority(1).hidden(false);
                 tokenize(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS).pattern("UNICODE_CHARACTER_CLASS").priority(1).hidden(false);
                 tokenize(KEY_ENV).pattern("env").priority(0).hidden(false);
                 tokenize(KEY_IMPORT).pattern("import").priority(0).hidden(false);
                 tokenize(KEY_CODE).pattern("code").priority(0).hidden(false);
                 tokenize(KEY_SYNTAX).pattern("syntax").priority(0).hidden(false);
                 tokenize(DELIMITER).pattern(";").priority(0).hidden(false);
                 tokenize(NON_TERMINAL).pattern("[a-z][a-z0-9_]*").priority(-1).hidden(false);
                 tokenize(LOP_OR).pattern("\\|").priority(0).hidden(false);
                 tokenize(LEFT_FIGURE_BRACKET).pattern("\\{").priority(0).hidden(false);
                 tokenize(RIGHT_FIGURE_BRACKET).pattern("\\}").priority(0).hidden(false);
                 tokenize(OP_MULTIPLY).pattern("\\*").priority(0).hidden(false);
                 tokenize(LEFT_BRACKET).pattern("\\(").priority(0).hidden(false);
                 tokenize(RIGHT_BRACKET).pattern("\\)").priority(0).hidden(false);
                 tokenize(LEFT_INLINE_ACTION_BRACKET).pattern("<").priority(0).hidden(false);
                 tokenize(RIGHT_INLINE_ACTION_BRACKET).pattern(">").priority(0).hidden(false);
                 tokenize(OP_ASSIGN).pattern("=").priority(0).hidden(false);
                 tokenize(OP_SUM).pattern("\\+").priority(0).hidden(false);
                 tokenize(LAMBDA).pattern("->").priority(0).hidden(false);
                 tokenize(WS).pattern("\\s|\\n|\\r").priority(2147483647).hidden(true);
                 tokenize(SINGLE_LINE_COMMENT).pattern("//.*(\n|\r|\r\n|\n\r)").priority(2147483647).hidden(true);
                 tokenize(MULTI_LINE_COMMENT).pattern("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/").priority(2147483647).hidden(true);
             }
         });


    public TdfParserImpl(
        final MetaInf meta,
        final Predictor predictor
    ) {
        super(meta, predictor);
    }

    private final Processor<String> stringProcessor = new StringProcessor();
    private final Stack<LetterConstructor> letters = new Stack<>();
    private final Stack<EnvironmentConstructor> environments = new Stack<>();
    private final Stack<ProductionConstructor> productions = new Stack<>();
    private LexerAbstractModule lexerModule;
    private org.tdf4j.core.module.ParserAbstractModule parserModule;

    @Override
    public LexerAbstractModule getLexerModule() {
       return this.lexerModule;
    }

    @Override
    public ParserAbstractModule getParserModule() {
       return this.parserModule;
    }


    @Override
    public AST parse(final CharSequence input) {
        this.stream = new BufferedStream<>(lexer.analyze(input));
        this.ast = AST.create(tdf_lang);
        tdf_lang.call();
        return ast;
    }

    private void ebnf_optional() {
        match(LEFT_SQUARE_BRACKET);
        call(ebnf_elements_set);
        match(RIGHT_SQUARE_BRACKET);
    }

    private void ebnf_elements_set() {
        call(ebnf_element);
        if(canReach(COMMA)) {
            match(COMMA);
            call(ebnf_elements_set);
        }
    }

    private void ebnf_or() {
        for(int i487805674 = 0; i487805674 < 2; i487805674++) {
            match(LOP_OR);
            call(ebnf_element);
        }
        while(true) {
            if(canReach(LOP_OR)) {
                match(LOP_OR);
                call(ebnf_element);
            } else {
                break;
            }
        }
    }

    private void tdf_lang() {
        this.lexerModule = new LexerAbstractModule() {
           @Override
           public void configure() {}
        };

        this.parserModule = new ParserAbstractModule() {
           @Override
           public void configure() {}
        };

        call(lexis);
        if(canReach(environment)) {
            call(environment);
        }
        call(syntax);
        match(EOF);
    }

    private void lexis() {
        match(KEY_LEXIS);
        while(true) {
            if(canReach(terminal_description)) {
                call(terminal_description);
            } else {
                break;
            }
        }
    }

    private void env_import() {
        match(KEY_IMPORT);
        match(STRING, token -> {
            environments.peek().addPackage(stringProcessor.process(token.getValue()));
        });
    }

    private void terminal_parameter_pattern_flag() {
        match(TERMINAL_PARAMETER_PATTERN_FLAG);
        match(COLON);
        call(pattern_flags);
    }

    private void terminal_parameter_priority() {
        match(TERMINAL_PARAMETER_PRIORITY);
        match(COLON);
        match(INTEGER, token -> {
            letters.peek().setPriority(token.getValue());
        });
    }

    private void production_description() {
        match(NON_TERMINAL, token -> {
            productions.push(new ProductionConstructor(parserModule.prod(token.getValue())));
        });
        match(OP_ASSIGN);
        call(ebnf_elements_set, node -> {
            productions.peek().setElements(node);
        });
        match(DELIMITER);
        productions.pop().construct();

    }

    private void ebnf_terminal() {
        match(TERMINAL_TAG);
        if(canReach(LAMBDA)) {
            match(LAMBDA);
            match(STRING);
        }
    }

    private void pattern_flags() {
        switch(predict(
                new Alt(0, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES"),
                new Alt(1, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE"),
                new Alt(2, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS"),
                new Alt(3, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE"),
                new Alt(4, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL"),
                new Alt(5, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL"),
                new Alt(6, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE"),
                new Alt(7, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ"),
                new Alt(8, "TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS"))
        ) {
            case 0 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNIX_LINES);
                    break;
                }
            case 1 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CASE_INSENSITIVE);
                    break;
                }
            case 2 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_COMMENTS);
                    break;
                }
            case 3 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_MULTILINE);
                    break;
                }
            case 4 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_LITERAL);
                    break;
                }
            case 5 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_DOTALL);
                    break;
                }
            case 6 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CASE);
                    break;
                }
            case 7 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_CANON_EQ);
                    break;
                }
            case 8 : {
                    match(TERMINAL_PARAMETER_PATTERN_FLAG_VALUE_UNICODE_CHARACTER_CLASS);
                    break;
                }
        }
        letters.peek().addFlag(ast.getLastLeaf().getToken().getValue());

        if(canReach(OP_SUM)) {
            match(OP_SUM);
            call(pattern_flags);
        }
    }

    private void environment() {
        match(KEY_ENV);
        environments.push(new EnvironmentConstructor(parserModule.environment()));

        while(true) {
            if(canReach(env_import)) {
                call(env_import);
            } else {
                break;
            }
        }
        if(canReach(env_code)) {
            call(env_code);
        }
        environments.pop().construct();

    }

    private void ebnf_non_terminal() {
        match(NON_TERMINAL);
        if(canReach(LAMBDA)) {
            match(LAMBDA);
            match(STRING);
        }
    }

    private void terminal_parameters_values() {
        switch(predict(
                new Alt(0, "terminal_parameter_priority"),
                new Alt(1, "terminal_parameter_hidden"),
                new Alt(2, "terminal_parameter_pattern_flag"))
        ) {
            case 0 : {
                    call(terminal_parameter_priority);
                    break;
                }
            case 1 : {
                    call(terminal_parameter_hidden);
                    break;
                }
            case 2 : {
                    call(terminal_parameter_pattern_flag);
                    break;
                }
        }
        if(canReach(COMMA)) {
            match(COMMA);
            call(terminal_parameters_values);
        }
    }

    private void ebnf_repetition() {
        match(INTEGER);
        match(OP_MULTIPLY);
        call(ebnf_element);
    }

    private void ebnf_group() {
        match(LEFT_BRACKET);
        call(ebnf_elements_set);
        match(RIGHT_BRACKET);
    }

    private void terminal_parameters() {
        match(LEFT_SQUARE_BRACKET);
        call(terminal_parameters_values);
        match(RIGHT_SQUARE_BRACKET);
    }

    private void env_code() {
        match(KEY_CODE);
        match(STRING, token -> {
            environments.peek().setCode(stringProcessor.process(token.getValue()));
        });
    }

    private void ebnf_repeat() {
        match(LEFT_FIGURE_BRACKET);
        call(ebnf_elements_set);
        match(RIGHT_FIGURE_BRACKET);
    }

    private void syntax() {
        match(KEY_SYNTAX);
        while(true) {
            if(canReach(production_description)) {
                call(production_description);
            } else {
                break;
            }
        }
    }

    private void ebnf_element() {
        switch(predict(
                new Alt(0, "ebnf_optional"),
                new Alt(1, "ebnf_or"),
                new Alt(2, "ebnf_repeat"),
                new Alt(3, "ebnf_repetition"),
                new Alt(4, "ebnf_group"),
                new Alt(5, "ebnf_terminal"),
                new Alt(6, "ebnf_non_terminal"),
                new Alt(7, "ebnf_inline_action"))
        ) {
            case 0 : {
                    call(ebnf_optional);
                    break;
                }
            case 1 : {
                    call(ebnf_or);
                    break;
                }
            case 2 : {
                    call(ebnf_repeat);
                    break;
                }
            case 3 : {
                    call(ebnf_repetition);
                    break;
                }
            case 4 : {
                    call(ebnf_group);
                    break;
                }
            case 5 : {
                    call(ebnf_terminal);
                    break;
                }
            case 6 : {
                    call(ebnf_non_terminal);
                    break;
                }
            case 7 : {
                    call(ebnf_inline_action);
                    break;
                }
        }
    }

    private void ebnf_inline_action() {
        match(LEFT_INLINE_ACTION_BRACKET);
        match(STRING);
        match(RIGHT_INLINE_ACTION_BRACKET);
    }

    private void terminal_parameter_hidden() {
        match(TERMINAL_PARAMETER_HIDDEN);
        match(COLON);
        match(BOOLEAN, token -> {
            letters.peek().setHidden(token.getValue());
        });
    }

    private void terminal_description() {
        match(TERMINAL_TAG, token -> {
            letters.push(new LetterConstructor(lexerModule.tokenize(token.getValue())));
        });
        match(STRING, token -> {
            letters.peek().setPattern(stringProcessor.process(token.getValue()));
        });
        if(canReach(terminal_parameters)) {
            call(terminal_parameters);
        }
        letters.pop().construct();

    }


}