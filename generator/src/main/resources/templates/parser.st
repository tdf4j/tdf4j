package <package>;

<imports>

public class <className> implements Parser {

    @Override
    public AST parse(final Stream\<Token> tokens) {
        tokens.forEach(token -> {
            System.out.println(token.tag() + " : " + token.value());
        });
        return null;
    }

    @Override
    public AST parse(final List\<Token> tokens) {
        tokens.forEach(token -> {
            System.out.println(token.tag() + " : " + token.value());
        });
        return null;
    }
}