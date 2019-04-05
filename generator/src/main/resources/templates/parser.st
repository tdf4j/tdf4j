package <package>;

<imports>

public class <className> implements Parser {
    private final First first;
    private final Follow follow;
    private BufferedStream\<Token> stream;
    private Stack\<Anchor> streamAnchors;

    public <className>(final First first, final Follow follow) {
        this.first = first;
        this.follow = follow;
    }

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

    private List\<String> predict(final Token token) {
        if(token == null) {
            return new ArrayList\<>();
        }
        final List\<String> predictions = new ArrayList\<>();
        first.set().forEach((ident, set) -> {
            if(set.stream().map(Terminal.Tag::value).collect(Collectors.toSet()).contains(token.tag())) {
                predictions.add(ident);
            }
        });
        predictions.add(token.tag());
        return predictions;
    }

    private boolean contains(final List\<String> predictions, final String element) {
        return predictions.contains(element);
    }
}