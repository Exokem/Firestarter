package xkv.content;

import xkv.visual.panels.DynamicResizeable;

public class Track
{
    protected String identifier, author;

    protected int plays;

    protected long duration;

    protected Track(String identifier, String author, long duration)
    {
        this.identifier = identifier;
        this.author = author;
        this.plays = 0;
        this.duration = duration;
    }

    public static Track compose(String identifier, String author, long duration)
    {
        return new Track(identifier, author, duration);
    }
}
