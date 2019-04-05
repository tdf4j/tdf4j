//<comment>
{
    try {
        streamAnchors.push(stream.setAnchor());
        if(<firstElements: {firstElement|predict(stream.peek()).contains("<firstElement>")}; separator=" ||\n">) {
            <codeBlocks: {codeBlock|<codeBlock>};separator="\n">
            streamAnchors.pop();
        }
     } catch (UnexpectedTokenException e<hash>) {
         stream.revert(streamAnchors.pop());
         throw e<hash>;
     }
}