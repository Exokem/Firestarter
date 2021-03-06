package xkv.visual.panels.audion;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import xkv.content.Album;
import xkv.content.Track;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import java.util.ArrayList;
import java.util.List;

public class AudionPanel extends StandardGridPane implements IStylable
{
    public static AudionPanel instance()
    {
        return new AudionPanel();
    }

    protected static final StandardGridPane CONTENT_OVERARCH = PaneFactory.paddedGrid(10, 4, 4, Style.INSET);

    private final DynamicResizeable root;

    protected static int createdAlbums = 0;

    public static final double PANEL_WIDTH = 1280;
    public static final double PANEL_HEIGHT = 760;

    public static final Album LIBRARY = Album.empty("Library");

    private AudionPanel()
    {
        super();

        this.pad(3, 3);

        root = DynamicResizeable.root(this, (layoutContent) -> {});

        GridPane.setHgrow(CONTENT_OVERARCH, Priority.ALWAYS);
        GridPane.setVgrow(CONTENT_OVERARCH, Priority.ALWAYS);

        CONTENT_OVERARCH.setPrefWidth(PANEL_WIDTH);
        CONTENT_OVERARCH.setPrefHeight(PANEL_HEIGHT);

        CONTENT_OVERARCH.add(AudionAlbumSelect.albumProvider(), 1, 1);
        CONTENT_OVERARCH.add(AudionAlbumSelect.ALBUM_LIST, 1, 2);

        CONTENT_OVERARCH.add(AudionAlbumView.albumContentPanel(), 2, 1, 1, 2, Priority.SOMETIMES);

        this.add(CONTENT_OVERARCH, 1, 1);

        // Debug

        addAlbum(LIBRARY);

        Track testTrack = Track.compose("Elephant", "Tame Impala", null);
        Track track2 = Track.compose("Borderline", "Tame Impala", null);

        LIBRARY.addTrack(testTrack);
        LIBRARY.addTrack(track2);
    }

    protected static final List<Album> albums = new ArrayList<>();

    protected static StyledButton addAlbum(Album album)
    {
        StyledButton button = new StyledButton(album.displayName());

        button.setOnAction(value -> AudionAlbumView.openAlbum(button, album));

        albums.add(album);

        AudionAlbumSelect.ALBUM_LIST.link(button, album);

        createdAlbums ++;

        return button;
    }

    protected double scaledWidth(double scale)
    {
        return CONTENT_OVERARCH.getWidth() * scale;
    }
}
