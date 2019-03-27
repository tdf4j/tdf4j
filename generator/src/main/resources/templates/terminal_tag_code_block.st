if(!stream.next().value().equalsIgnoreCase("<terminal>")) {
    throw new RuntimeException("Unexpected token: <terminal>");
}