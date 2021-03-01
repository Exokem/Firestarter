package xkv.content;

public class Track
{
    protected String identifier, author;

    protected int plays;

    protected long duration;

    private Track()
    {

    }

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

    public static Track empty()
    {
        return new Track();
    }
}
