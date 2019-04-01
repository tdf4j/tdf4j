//<comment>
if(stream.peek() != null && stream.peek().tag().equalsIgnoreCase("<terminal>")) {
    stream.next();
} else {
    throw new UnexpectedTokenException(stream.peek());
}