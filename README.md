# tdf4j [![Build Status](https://travis-ci.com/tdf4j/tdf4j.svg?branch=master)](https://travis-ci.com/tdf4j/tdf4j) ![codecov](https://codecov.io/gh/tdf4j/tdf4j/branch/master/graphs/badge.svg) ![license](https://img.shields.io/github/license/tdf4j/tdf4j.svg) [![Maven Central](https://img.shields.io/maven-central/v/io.github.tdf4j/tdf4j.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.tdf4j%22%20AND%20a:%22tdf4j%22)
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
    <groupId>io.github.tdf4j</groupId>
    <artifactId>generator</artifactId>
    <version>1.0.1</version>
</dependency>
```

How to configure and use analyzers can be found on following pages:
+ [**Lexical analyzer**](https://github.com/therealmonE/tdf4j/tree/master/lexer)
+ [**Syntax analyzer**](https://github.com/therealmonE/tdf4j/tree/master/parser)
