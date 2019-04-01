package <package>;

<imports>

public class <className> implements Parser {
    private BufferedStream\<Token> stream;
    private Stack\<Anchor> streamAnchors;

    @Override
    public AST parse(final Stream\<Token> tokens) {
        this.stream = new BufferedStream\<>(tokens);
        this.streamAnchors = new Stack\<>();
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