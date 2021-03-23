package xkv.content;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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
import xkv.Firestarter;
import xkv.visual.controls.DynamicButton;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.AudionPanel;

import java.io.File;
import java.io.IOException;

import static xkv.ResourceLoader.TRK_HOV;
import static xkv.ResourceLoader.TRK_ICN;

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
        final double reference = AudionPanel.PANEL_HEIGHT / 20;

        StandardGridPane container = PaneFactory.autoPaddedGrid(0, 2, 1, Style.SCROLLPANE_BUTTON);

        DynamicButton play = DynamicButton.configure(TRK_ICN, TRK_HOV).configureTooltip("Play this track");
        StyledButton identifierView = new StyledButton(identifier);
        StyledButton authorView = new StyledButton(author);
        StyledButton duration = new StyledButton("0:00");

        play.resize(reference);

        DynamicResizeable.addResizeListener(() -> play.resize(container.getHeight()));

        identifierView.setMaxWidth(Double.MAX_VALUE);
        identifierView.setAlignment(Pos.CENTER_LEFT);

        authorView.setPrefWidth(reference * 5);
        authorView.setAlignment(Pos.CENTER_LEFT);

        duration.setMaxWidth(reference * 3);
        duration.setMinWidth(reference * 3);
        duration.setAlignment(Pos.CENTER_RIGHT);

        DynamicResizeable.addResizeListener(() -> authorView.setPrefWidth(Firestarter.container.getWidth() / 30 * 5));

        container.add(play, 1, 1, Priority.NEVER);
        container.add(identifierView, 2, 1, Priority.ALWAYS);
        container.add(authorView, 3, 1, Priority.SOMETIMES);
        container.add(duration, 4, 1, Priority.SOMETIMES);

        container.setMaxHeight(reference);
        container.setCache(false);

        container.getChildren().forEach(node ->
        {
            if (node instanceof IStylable)
            {
                IStylable stylable = (IStylable) node;

                stylable.addVisualStyle(Style.SPBC);
            }

            if (node instanceof StyledButton)
            {
                StyledButton button = (StyledButton) node;
                button.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(button, Priority.ALWAYS);
                button.setCache(false);
            }
        });

        return container;
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
