package xkv.content;

import javafx.scene.image.Image;
import xkv.util.Time;
import xkv.visual.VisualResourceLoader;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Album
{
    private final List<Track> tracks = new ArrayList<>();

    private final Date created;

    protected final String identifier;

    protected String displayName;

    protected Image image;

    protected long duration = 0;

    protected Album(String identifier)
    {
        this.identifier = identifier;
        this.displayName = identifier;
        this.created = Date.from(Instant.now());
    }

    public static Album empty(String identifier)
    {
        return new Album(identifier);
    }

    public static void copy(Album source, Album destination)
    {
        destination.tracks.addAll(source.tracks);
    }

    public Image image()
    {
        return image;
    }

    public String displayName()
    {
        return displayName;
    }

    public String formattedDate()
    {
        return String.format("Created: %s", created.toString());
    }

    public String formattedInfo()
    {
        String info = String.format("%d track%s", tracks.size(), tracks.size() == 1 ? "" : "s");

        String time = Time.longToString(duration, false);

        if (!time.isEmpty())
        {
            info = String.format("%s, %s", info, time);
        }

        return info;
    }

    public String formattedSpan()
    {
        return String.format("%s\n%s", formattedDate(), formattedInfo());
    }

    public Album configureImage(Image image)
    {
        if (image != null)
        {
            this.image = image;
        }
        else
        {
            this.image = VisualResourceLoader.DEFAULT_IMAGE;
        }

        return this;
    }

    public void addTrack(Track track)
    {
        tracks.add(track);
    }

    public void removeTrack(Track track)
    {

    }

    public void removeTrack(int index)
    {

    }

    public void configureDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public void rename(String newIdentifier)
    {
        this.displayName = newIdentifier;
    }

    public Iterator<Track> iterator()
    {
        return tracks.iterator();
    }

    @Override
    public int hashCode()
    {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Album)
        {
            Album other = (Album) obj;

            if (other.identifier.equals(this.identifier))
            {
                return other.tracks.equals(this.tracks);
            }
        }

        return false;
    }
}
