package xkv.visual.panels.audion;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import xkv.content.Album;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PanelFactory;
import xkv.visual.panels.StandardGridPane;

import java.util.ArrayList;
import java.util.List;

public class AudionPanel extends StandardGridPane implements IStylable
{
    public static AudionPanel instance()
    {
        return new AudionPanel();
    }

    protected static final StandardGridPane CONTENT_OVERARCH = PanelFactory.paddedGrid(10, 4, 4, Style.INSET);

    private final DynamicResizeable root;

    public static final double PANEL_WIDTH = 1280;
    private static final double panelHeight = 760;

    private AudionPanel()
    {
        super();

        this.pad(3, 3);

        root = DynamicResizeable.root(this, (layoutContent) -> {});

        GridPane.setHgrow(CONTENT_OVERARCH, Priority.ALWAYS);
        GridPane.setVgrow(CONTENT_OVERARCH, Priority.ALWAYS);

        CONTENT_OVERARCH.setPrefWidth(PANEL_WIDTH);
        CONTENT_OVERARCH.setPrefHeight(panelHeight);

        CONTENT_OVERARCH.add(AudionAlbumSelect.albumProvider(), 1, 1);
        CONTENT_OVERARCH.add(AudionAlbumSelect.ALBUM_LIST, 1, 2);

        CONTENT_OVERARCH.add(AudionAlbumView.albumContentPanel(), 2, 1, 1, 2, Priority.SOMETIMES);

        this.add(CONTENT_OVERARCH, 1, 1);
    }

    protected static final List<Album> albums = new ArrayList<>();

    protected static StyledButton addAlbum(Album album)
    {
        StyledButton button = new StyledButton(album.displayName());

        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(panelHeight / 20);
        button.setOnAction(value -> AudionAlbumView.openAlbum(button, album));

        albums.add(album);

        AudionAlbumSelect.ALBUM_LIST.link(button, album);

        return button;
    }

    protected double scaledWidth(double scale)
    {
        return CONTENT_OVERARCH.getWidth() * scale;
    }
}
