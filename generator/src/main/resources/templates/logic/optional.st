//<comment>
{
    streamAnchors.push(stream.setAnchor());
    try {
        <codeBlocks: {codeBlock|<codeBlock>};separator="\n">
        streamAnchors.pop();
    } catch (UnexpectedTokenException e<hash>) {
        stream.revert(streamAnchors.pop());
    }
}