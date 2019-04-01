//<comment>
if(stream.peek() == null ||
    <exceptions: {exception|stream.peek().tag().equalsIgnoreCase("<exception>")};separator=" ||\n">
) {
    throw new UnexpectedTokenException(stream.peek());
} else {
    stream.next();
}