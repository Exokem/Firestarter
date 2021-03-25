package xkv.content;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;
import org.json.JSONException;
import org.json.JSONObject;
import xkv.visual.controls.TrackDisplay;
import xkv.visual.panels.StandardGridPane;

import java.io.File;
import java.io.IOException;

public class Track
{
    public static Track fromFile(File file)
    {
        Track track = new Track();

        track.data = file;
        track.path = file.getAbsolutePath();

        try
        {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag audioTag = audioFile.getTag();
            TagField identifierField = audioTag.getFirstField(FieldKey.TITLE);
            TagField authorField = audioTag.getFirstField(FieldKey.ARTIST);
            AudioHeader header = audioFile.getAudioHeader();
            track.duration = header.getTrackLength();

            if (identifierField != null)
            {
                track.identifier = identifierField.toString();
            }

            if (authorField != null)
            {
                track.author = authorField.toString();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (CannotReadException e)
        {
            e.printStackTrace();
        } catch (ReadOnlyFileException e)
        {
            e.printStackTrace();
        } catch (TagException e)
        {
            e.printStackTrace();
        } catch (InvalidAudioFrameException e)
        {
            e.printStackTrace();
        }


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

    public long duration()
    {
        return duration;
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
        JSONObject attributes = trackJson.getJSONObject(AT_KEY);

        if (!attributes.has(FD_KEY)) return null;

        Track track = Track.fromFile(new File(attributes.getString(FD_KEY)));

        if (!track.data.exists()) return null;

        track.plays = attributes.getInt(PY_KEY);
        track.identifier = attributes.getString(ID_KEY);
        track.author = attributes.getString(AU_KEY);
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
