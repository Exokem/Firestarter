package xkv.content;

import javafx.scene.image.Image;
import xkv.visual.VisualResourceLoader;

import java.util.ArrayList;
import java.util.List;

public class Album
{
    private final List<Track> tracks = new ArrayList<>();

    protected final String identifier;

    protected String displayName;

    protected Image image;

    protected Album(String identifier)
    {
        this.identifier = identifier;
        this.displayName = identifier;
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

    public void configureDisplayName(String displayName)
    {
        this.displayName = displayName;
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

    public void rename(String newIdentifier)
    {
        this.displayName = newIdentifier;
    }

    public long totalDuration()
    {
        long total = 0;

        for (Track track : tracks)
        {
            total += track.duration;
        }

        return total;
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
