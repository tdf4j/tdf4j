# tdf4j
Programming languages are the way to describe process of
computing for different devices. Modern world highly 
depends on programming languages because almost every
software on every device in the world written on one of
programming languages. But before executing program
it should be represented in a form that will be understood
by the device. Such representation program components
are called translators.

**Tdf4j** (translator description framework for _java_) is a 
framework for _java programing language_ which can help 
to write own translator. The process of translating 
languages includes two main phases: lexical and syntax
analysis. This framework provides classes for generating
both lexical and syntax analyzers.

## Usage
Add a dependency to pom file.
```xml
<dependency>
    <groupId>io.github.therealmone.tdf4j</groupId>
    <artifactId>generator</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

How to configure and use analyzers can be found on following pages:
+ [**Lexical analyzer**](https://github.com/therealmonE/tdf4j/tree/master/lexer)
+ [**Syntax analyzer**](https://github.com/therealmonE/tdf4j/tree/master/parser)