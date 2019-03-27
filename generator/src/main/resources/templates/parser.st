package <package>;

<imports>

public class <className> implements Parser {
    private Stream\<Token> stream;

    @Override
    public AST parse(final Stream\<Token> tokens) {
        this.stream = tokens;
        <initProd>();
        return new AST.Builder().build();
    }

    @Override
    public AST parse(final List\<Token> tokens) {
        tokens.forEach(token -> {
            System.out.println(token.tag() + " : " + token.value());
        });
        return null;
    }

    <methods: {method|<method><\n><\n>}>
}