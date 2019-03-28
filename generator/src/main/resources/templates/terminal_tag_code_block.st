//<elementDeclaration>
{
    if(stream.peek().value().equalsIgnoreCase("<terminal>")) {
        stream.next();
    } else {
        throw new RuntimeException("Unexpected token: " + stream.peek());
    }
}