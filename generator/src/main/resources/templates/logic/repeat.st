//<comment>
{
    while(true) {
        try {
            streamAnchors.push(stream.setAnchor());
            if(<firstElements: {firstElement|predict(stream.peek()).contains("<firstElement>")}; separator=" ||\n">) {
                <codeBlocks: {codeBlock|<codeBlock>};separator="\n">
                streamAnchors.pop();
            } else {
                streamAnchors.pop();
                break;
            }
        } catch (UnexpectedTokenException e<hash>) {
            stream.revert(streamAnchors.pop());
            throw e<hash>;
        }
    }
}