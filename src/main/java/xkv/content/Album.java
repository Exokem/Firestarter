package xkv.content;

import javafx.scene.image.Image;
import xkv.visual.VisualResourceLoader;

import java.util.ArrayList;
import java.util.List;

public class Album
{
    private final List<Track> tracks = new ArrayList<>();

    protected String identifier;

    protected Image image;

    protected Album(String identifier)
    {
        this.identifier = identifier;
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

    public String identifier()
    {
        return identifier;
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

    public long totalDuration()
    {
        long total = 0;

        for (Track track : tracks)
        {
            total += track.duration;
        }

        return total;
    }



//    public String formatDuration()
//    {
//        long duration = totalDuration();
//
//
//    }


}
