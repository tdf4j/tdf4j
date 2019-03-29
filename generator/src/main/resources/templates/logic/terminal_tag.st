//<comment>
if(stream.peek() != null && stream.peek().value().equalsIgnoreCase("<terminal>")) {
    stream.next();
} else {
    throw new RuntimeException("Unexpected token: " + String.valueOf(stream.peek()));
}