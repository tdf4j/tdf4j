//<comment>
{
    try {
        while(true) {
            streamAnchors.push(stream.setAnchor());
            <codeBlocks: {codeBlock|<codeBlock>};separator="\n">
            streamAnchors.pop();
        }
    } catch(RuntimeException e<hash>) {
        stream.revert(streamAnchors.pop());
    }
}