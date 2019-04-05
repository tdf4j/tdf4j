//<comment>
{
    if(stream.peek() != null && (
        <firstStartElements: {startElement|predict(stream.peek()).contains("<startElement>")}; separator=" ||\n">
    )) {
        <firstCodeBlocks: {codeBlock|<codeBlock>};separator="\n">
    } else if(stream.peek() != null && (
        <secondStartElements: {startElement|predict(stream.peek()).contains("<startElement>")}; separator=" ||\n">
    )) {
        <secondCodeBlocks: {codeBlock|<codeBlock>};separator="\n">
    } else {
        throw new UnexpectedTokenException(stream.peek());
    }
}