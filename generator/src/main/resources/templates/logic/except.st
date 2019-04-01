//<comment>
if(stream.peek() == null ||
    <exceptions: {exception|stream.peek().tag().equalsIgnoreCase("<exception>")};separator=" ||\n">
) {
    throw new RuntimeException("Unexpected token: " + String.valueOf(stream.peek()));
} else {
    stream.next();
}