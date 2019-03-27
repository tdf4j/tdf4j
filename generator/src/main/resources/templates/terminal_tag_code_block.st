//<elementDeclaration>
{
    if(stream.next().value().equalsIgnoreCase("<terminal>")) {
        stream.removeTop();
    } else {
        throw new RuntimeException("Unexpected token: " + stream.next());
    }
}