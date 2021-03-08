package xkv.content;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.BorderPane;
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
import xkv.Firestarter;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;
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

            Tag audioTag = audioFile.getTag();

            track.identifier = audioTag.getFirstField(FieldKey.TITLE).toString();
            track.author = audioTag.getFirstField(FieldKey.ALBUM_ARTIST).toString();

//            String seconds = audioTag.getFirstField(GenericAudioHeader.FIELD_LENGTH).toString();
//            System.out.println(seconds);

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
        }


        track.data = file;

        return track;
    }

    protected String identifier, author;

    protected int plays;

    protected File data;

    protected String path;

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

        StyledImageView play = new StyledImageView(TRK_ICN);
        StyledButton identifierView = new StyledButton(identifier);
        StyledButton authorView = new StyledButton(author);
        StyledButton duration = new StyledButton("0:00");

        BorderPane playContainer = PaneFactory.styledBorderPane(play, Style.SPBC);

        playContainer.setPrefWidth(reference);

        play.configureTooltip("Play this track");
        play.configureHover(TRK_ICN, TRK_HOV);

        VisualResourceLoader.scaleImageView(play, reference - 10);
        GridPane.setHalignment(playContainer, HPos.CENTER);
        GridPane.setValignment(playContainer, VPos.CENTER);

        identifierView.setMaxWidth(Double.MAX_VALUE);
        identifierView.setAlignment(Pos.CENTER_LEFT);

        authorView.setPrefWidth(reference * 5);
        authorView.setAlignment(Pos.CENTER_LEFT);

        duration.setMaxWidth(reference * 3);
        duration.setMinWidth(reference * 3);
        duration.setAlignment(Pos.CENTER_RIGHT);

        DynamicResizeable.addResizeListener(() -> authorView.setPrefWidth(Firestarter.container.getWidth() / 30 * 5));

        container.add(playContainer, 1, 1, Priority.NEVER);
        container.add(identifierView, 2, 1, Priority.ALWAYS);
        container.add(authorView, 3, 1, Priority.SOMETIMES);
        container.add(duration, 4, 1, Priority.SOMETIMES);

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
