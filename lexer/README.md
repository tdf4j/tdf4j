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
 
Terminals consist of `tag`, `pattern`, and `priority`.
- Tag is a `String` which defines the name of parsed token.
- Pattern is a `Regular expression` which token should match.
- Priority is an `Integer` which uses for collision resolution. By default: 0.
(For example there is 2 terminals: `tag: VAR, pattern: ^[a-z]+$` and `tag: for, pattern: ^for$`, so input `"for"` matches both
terminals. To avoid this specify priorities for terminals: `tag: var, pattern ^[a-z]+$, priority: 0` and `tag: FOR, pattern: ^for$, priority: 1`.
Now input `"for"` will match terminal with tag `FOR`)
 
There is three ways for configuration:
##### 1. In code
Lexer accepts ```AbstractLexerModule``` as configuration class. To configure lexer by code you must extend this class:
```java
    class Configuration extends AbstractLexerModule {
        @Override
        public void configure() {
            tokenize("tag").pattern("pattern").priority(1);
            tokenize("tag").pattern("pattern");
            tokenize("tag").pattern("pattern").priority(10000);
        }
    }
```
Priority is not necessary element. By default it is 0.

##### 2. XML
Xml file must contains `<terminals>` as root element and `<terminal tag="tag" pattern="pattern" priority="100"/>` as child.
Example:
```xml
    <terminals>
      <terminal tag="tag1" pattern="pattern1" priority="1"/>
      <terminal tag="tag2" pattern="pattern2"/>
      <terminal tag="tag3" pattern="pattern3" priority="10000"/>
    </terminals>
```
Priority is not necessary element. By default it is 0.

##### 3. JSON
```json
    {
      "terminals": [
        {"tag": "tag1", "pattern": "pattern1", "priority": 1},
        {"tag": "tag2", "pattern": "pattern2"},
        {"tag": "tag3", "pattern": "pattern3", "priority": 10000}
      ]
    }
```
Priority is not necessary element. By default it is 0.

### Lexer factory
Lexer factory can build lexer for each type of configuration:
- In Code: 
```java
    Lexer lexer = new LexerFactory().withModule(new Configuration());
```
- Xml:
```java
    Lexer lexer = new LexerFactory().fromXml(new File("terminals.xml"));
    Lexer lexer = new LexerFactory().fromXml(new FileInputStream(new File("terminals.xml")));
    Lexer lexer = new LexerFactory().fromXml("<terminals></terminals>");
```
- Json:
```java
    Lexer lexer = new LexerFactory().fromXml(new File("terminals.json"));
    Lexer lexer = new LexerFactory().fromXml(new FileInputStream(new File("terminals.json")));
    Lexer lexer = new LexerFactory().fromXml("{\"terminals:\"[]}");
```
