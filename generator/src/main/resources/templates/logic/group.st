//<comment>
{
    streamAnchors.push(stream.setAnchor());
    try {
        <codeBlocks: {codeBlock|<codeBlock>};separator="\n">
        streamAnchors.pop();
    } catch(RuntimeException e) {
        stream.revert(streamAnchors.pop());
        throw e;
    }
}