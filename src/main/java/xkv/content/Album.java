package xkv.content;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xkv.data.VisualResourceLoader;
import xkv.util.Time;

import java.time.Instant;
import java.util.*;

public class Album
{
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

    public String identifier()
    {
        return identifier;
    }

    private final List<Track> tracks = new ArrayList<>();
    private Date created;

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

    private static final String
            IN_KEY = "icon", AT_KEY = "attributes", TK_KEY = "tracks",
            ID_KEY = "identifier", DN_KEY = "display_name", DC_KEY = "instanced";

    public JSONObject serialize()
    {
        JSONObject attributes = new JSONObject();
        attributes.put(ID_KEY, identifier);
        attributes.put(DN_KEY, displayName);
        attributes.put(DC_KEY, created.getTime());
        attributes.put(IN_KEY, image.getUrl());

        JSONArray trackArray = new JSONArray();
        tracks.forEach(track -> trackArray.put(track.serialize()));

        JSONObject albumJson = new JSONObject();
        albumJson.put(AT_KEY, attributes);
        albumJson.put(TK_KEY, trackArray);

        return albumJson;
    }

    public static Album deserialize(JSONObject albumJson) throws JSONException
    {
        JSONObject attributes = albumJson.getJSONObject(AT_KEY);
        Album album = new Album(attributes.getString(ID_KEY));
        album.displayName = attributes.getString(DN_KEY);
        album.created = new Date(attributes.getLong(DC_KEY));
        try
        {
            album.image = new Image(attributes.getString(IN_KEY));
        }

        catch (JSONException ignored) {}

        JSONArray tracks = albumJson.getJSONArray(TK_KEY);
        for (int indx = 0; indx < tracks.length(); indx ++)
        {
            Track track = Track.deserialize(tracks.getJSONObject(indx));
            if (track != null) album.addTrack(track);
        }

        return album;
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

    public void addTracks(Collection<Track> tracks)
    {
        tracks.forEach(this::addTrack);
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
