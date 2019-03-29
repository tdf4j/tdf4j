//<comment>
{
    streamAnchors.push(stream.setAnchor());
    try {
         <codeBlocks1: {codeBlock|<codeBlock>};separator="\n">
         streamAnchors.pop();
    } catch (RuntimeException e<hash>) {
        stream.revert(streamAnchors.pop());
        <codeBlocks2: {codeBlock|<codeBlock>};separator="\n">
    }
}