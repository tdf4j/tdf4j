package io.github.therealmone.tdf4j.lexer.utils;

import io.github.therealmone.tdf4j.lexer.AbstractLexerConfig;

public class Config extends AbstractLexerConfig {
    @Override
    public void config() {
        tokenize("VAR").pattern("^[a-z]+$");
        tokenize("STRING").pattern("^\"(.*?)\"$");
        tokenize("NEW").pattern("^new$").priority(1);
        tokenize("TYPEOF").pattern("^typeof$").priority(1);
        tokenize("HASHSET").pattern("^hashset$").priority(1);
        tokenize("ARRAYLIST").pattern("^arraylist$").priority(1);
        tokenize("GET").pattern("^get$").priority(1);
        tokenize("SIZE").pattern("^size$").priority(1);
        tokenize("PUT").pattern("^put$").priority(1);
        tokenize("REMOVE").pattern("^remove$").priority(1);
        tokenize("REWRITE").pattern("^rewrite$").priority(1);
        tokenize("PRINT").pattern("^print$").priority(1);
        tokenize("COMMA").pattern("^,$");
        tokenize("CONCAT").pattern("^\\+\\+$").priority(2);
        tokenize("QUOTE").pattern("^\"$");
        tokenize("DIGIT").pattern("^-?(0|([1-9][0-9]*))$").priority(1);
        tokenize("DOUBLE").pattern("^-?((0[.][0-9]*)|([1-9][0-9]*[.][0-9]*))$");
        tokenize("ASSIGN_OP").pattern("^=$");
        tokenize("OP").pattern("^[\\+\\-\\/\\*]|(div)|(mod)$").priority(1);
        tokenize("DEL").pattern("^;$");
        tokenize("WHILE").pattern("^while$").priority(1);
        tokenize("IF").pattern("^if$").priority(1);
        tokenize("ELSE").pattern("^else$").priority(1);
        tokenize("DO").pattern("^do$").priority(1);
        tokenize("FOR").pattern("^for$").priority(1);
        tokenize("LOP").pattern("^[&\\|\\^\\!]$").priority(1);
        tokenize("COP").pattern("^[<>]|(<=)|(>=)|(==)|(!=)$").priority(1);
        tokenize("LB").pattern("^\\($");
        tokenize("RB").pattern("^\\)$");
        tokenize("FLB").pattern("^\\{$");
        tokenize("FRB").pattern("^\\}$");
        tokenize("$").pattern("^\\$$");
    }
}
