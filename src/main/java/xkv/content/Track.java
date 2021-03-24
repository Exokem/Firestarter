package xkv.content;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.json.JSONException;
import org.json.JSONObject;
import xkv.visual.controls.TrackDisplay;
import xkv.visual.panels.StandardGridPane;

import java.io.File;
import java.io.IOException;

public class Track
{
    public static Track compose(String identifier, String author, String path)
    {
        return new Track(identifier, author, path);
    }

    public static Track empty()
    {
        return new Track();
    }

    public static Track fromFile(File file)
    {
        Track track = new Track();

        try
        {
            AudioFile audioFile = AudioFileIO.read(file);
            track.path = file.getAbsolutePath();

            Tag audioTag = audioFile.getTag();

            track.identifier = audioTag.getFirstField(FieldKey.TITLE).toString();
            track.author = audioTag.getFirstField(FieldKey.ALBUM_ARTIST).toString();

        } catch (CannotReadException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (TagException e)
        {
            e.printStackTrace();
        } catch (ReadOnlyFileException e)
        {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e)
        {
            e.printStackTrace();
        } catch (NullPointerException ignored) {}


        track.data = file;

        return track;
    }

    public String identifier()
    {
        return identifier;
    }

    public String author()
    {
        return author;
    }

    public StandardGridPane forDisplay()
    {
        return display.getAndUpdate();
    }

    public File data()
    {
        return data;
    }

    private static final String
            TK_KEY = "track", AT_KEY = "attributes",
            ID_KEY = "identifier", AU_KEY = "author", PY_KEY = "iterations", FD_KEY = "path";

    public JSONObject serialize()
    {
        JSONObject attributes = new JSONObject();
        attributes.put(ID_KEY, identifier);
        attributes.put(AU_KEY, author);
        attributes.put(PY_KEY, plays);
        attributes.put(FD_KEY, path);

        JSONObject trackJson = new JSONObject();
        trackJson.put(AT_KEY, attributes);

        return trackJson;
    }

    public static Track deserialize(JSONObject trackJson) throws JSONException
    {
        Track track = empty();

        JSONObject attributes = trackJson.getJSONObject(AT_KEY);
        track.identifier = attributes.getString(ID_KEY);
        track.author = attributes.getString(AU_KEY);
        track.plays = attributes.getInt(PY_KEY);
        track.path = attributes.getString(FD_KEY);
        track.data = new File(track.path);

        return track;
    }

    protected String identifier = "Corrupted Track", author = "Unknown Author";
    protected int plays = 0;
    protected File data;
    protected String path = "";
    protected long duration;
    private final TrackDisplay display = new TrackDisplay(this);

    private Track()
    {

    }

    protected Track(String identifier, String author, String path)
    {
        this.identifier = identifier;
        this.author = author;
        this.plays = 0;
        this.path = path;
    }

    @Override
    public int hashCode()
    {
        return String.format("%s : %s", identifier, author).hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Track)
        {
            Track track = (Track) obj;

            return track.identifier.equals(this.identifier) && track.author.equals(this.author);
        }

        return false;
    }
}
