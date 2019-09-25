# Lexical analyzer
In computer science, lexical analysis, lexing or tokenization is the process of converting a sequence of characters 
(such as in a computer program or web page) into a sequence of tokens (strings with an assigned and thus identified meaning).
 A program that performs lexical analysis may be termed a lexer, tokenizer, or scanner, 
 though scanner is also a term for the first stage of a lexer. A lexer is generally combined with a parser, 
 which together analyze the syntax of programming languages, web pages, and so forth.
 
 [Wiki](https://en.wikipedia.org/wiki/Lexical_analysis)
 
# Usage
### Configuration
To use lexical analyzer you must specify which tokens it will parse. 
Lexer uses regular expressions for parsing. So you must describe
as known `terminals` to use lexer.

> In computer science, terminal and nonterminal symbols are the lexical elements used in specifying the production 
rules constituting a formal grammar. Terminal symbols are the elementary symbols of the language defined by a formal grammar.
 Nonterminal symbols (or syntactic variables) are replaced by groups of terminal symbols according to the production rules.
 
Terminals consist of **tag**, **pattern**, **priority** and **hidden flag**.
- **Tag** is a `String` which defines the name of parsed token.
- **Pattern** is a `Regular expression` which token should match.
- **Priority** is an `Integer` which uses for collision resolution. By default: 0.

    For example there is 2 terminals: `tag: VAR, pattern: ^[a-z]+$` and `tag: FOR, pattern: ^for$`, so input `"for"` matches both
    terminals. To avoid this specify priorities for terminals: `tag: var, pattern ^[a-z]+$, priority: 0` and `tag: FOR, pattern: ^for$, priority: 1`.
    Now input `"for"` will match terminal with tag `FOR`.
    
- **Hidden flag** is a `Boolean` that represents tokens which will be skipped. By default: false
 
    By default lexical analyzer
    doesnt skip any symbols. For example to make lexical analyzer skip any white spaces
    or new lines add next terminal: `tag: IGNORE, pattern: (\s|\n|\r)+, hidden: true`.
    (Also commentaries are usually skipped by lexical analyzer)
 
There is three ways for configuration:
##### 1. In code
Lexer accepts ```LexerAbstractModule``` as configuration class. To configure lexer by code you must extend this class:
```java
    class Configuration extends LexerAbstractModule {
        @Override
        public void configure() {
            tokenize("TAG1").pattern("pattern1").priority(1);
            tokenize("TAG2").pattern("pattern2");
            tokenize("TAG3").pattern("pattern3").priority(10000);
            tokenize("IGNORE").pattern("(\\s|\\n|\\r)+").hidden(true);
        }
    }
```

##### 2. XML
Xml file must contains `<terminals>` as root element and `<terminal tag="tag" pattern="pattern" priority="100"/>` as child.
Example:
```xml
    <terminals>
      <terminal tag="TAG1" pattern="pattern1" priority="1"/>
      <terminal tag="TAG2" pattern="pattern2"/>
      <terminal tag="TAG3" pattern="pattern3" priority="10000"/>
      <terminal tag="IGNORE" pattern="(\s|\n|\r)+" hidden="true"/>
    </terminals>
```

##### 3. JSON
```json
    {
      "terminals": [
        {"tag": "TAG1", "pattern": "pattern1", "priority": 1},
        {"tag": "TAG2", "pattern": "pattern2"},
        {"tag": "TAG3", "pattern": "pattern3", "priority": 10000},
        {"tag": "IGNORE", "pattern": "(\s|\n|\r)+", "hidden":  true}
      ]
    }
```

### Lexer generator
Lexer generator can build lexer for each type of configuration:
- In Code: 
```java
    Lexer lexer = LexerGenerator.newInstance().generate(new Configuration());
```
- Xml:
```java
    Lexer lexer = LexerGenerator.newInstance().fromXml(new File("terminals.xml"));
    Lexer lexer = LexerGenerator.newInstance().fromXml(new FileInputStream(new File("terminals.xml")));
    Lexer lexer = LexerGenerator.newInstance().fromXml("<terminals></terminals>");
```
- Json:
```java
    Lexer lexer = LexerGenerator.newInstance().fromJson(new File("terminals.json"));
    Lexer lexer = LexerGenerator.newInstance().fromJson(new FileInputStream(new File("terminals.json")));
    Lexer lexer = LexerGenerator.newInstance().fromJson("{\"terminals:\"[]}");
```
