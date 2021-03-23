package xkv.visual.panels.audion;

import javafx.scene.layout.Priority;
import xkv.content.Album;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Audion is the main logical container for the Firestarter media player.
 */
public final class Audion
{
    /* The externally accessible Audion Panel. */
    public static final StandardGridPane AUDION_PANEL = audionPanel();

    /**
     * Internally produces the specific {@link StandardGridPane} layout corresponding to the Audion panel.
     */
    private static StandardGridPane audionPanel()
    {
        StandardGridPane audionPanel = PaneFactory.autoPaddedGrid(10, 2, 3, Style.INSET);

        audionPanel.setPrefWidth(panelWidth());
        audionPanel.setPrefHeight(panelHeight());

        audionPanel.add(AudionAlbumSelect.ALBUM_PROVIDER, 1, 1);
        audionPanel.add(AudionAlbumSelect.ALBUM_LIST, 1, 2);

        audionPanel.latentAdd(AudionPlayer.View.ICON, 1 ,3);
        audionPanel.latentAdd(AudionPlayer.View.OVERLAY, 1, 3);

        audionPanel.add(AudionAlbumView.albumContentPanel(), 2, 1, 1, 3, Priority.ALWAYS);

        return audionPanel;
    }

    /**
     * Data contains internal values and data structures.
     */
    private static final class Data
    {
        // TODO: read dimensions from config

        /* The width and height of the Audion panel. */
        private static final double PANEL_WIDTH = 1280;
        private static final double PANEL_HEIGHT = 760;

        /* The list of all existent albums. */
        private static final List<Album> ALBUMS = new ArrayList<>();
    }

    /**
     * Allows external access to the panel width attribute stored in the Data class.
     * @return A double.
     */
    public static double panelWidth()
    {
        return Data.PANEL_WIDTH;
    }

    /**
     * Allows external access to the panel height attribute stored in the Data class.
     * @return A double.
     */
    public static double panelHeight()
    {
        return Data.PANEL_HEIGHT;
    }

    /**
     * Allows external access to the album measure attribute of the data class, the number of existent albums.
     * @return An integer.
     */
    public static int albumMeasure()
    {
        return Data.ALBUMS.size();
    }

    /**
     * Allows iteration over each existent album without providing the list of albums itself.
     *
     * @param consumer The action applicable to {@link Album} objects.
     */
    public static void forEachAlbum(Consumer<Album> consumer)
    {
        Data.ALBUMS.forEach(consumer);
    }

    /**
     * Allows remote removal of albums from the list of existent albums.
     *
     * @param album The album reference to be removed from existence.
     */
    public static void removeAlbum(Album album)
    {
        Data.ALBUMS.remove(album);
    }

    /**
     * Allows a specific {@link Album} instance to be added to the list of existent albums.
     *
     * TODO: adjust documentation once AudionAlbumSelect has been reformatted.
     *
     * @param album The {@link Album} to be added.
     * @return
     */
    public static StyledButton addAlbum(Album album)
    {
        StyledButton button = new StyledButton(album.displayName());
        button.setOnAction(value -> AudionAlbumView.openAlbum(button, album));

        Data.ALBUMS.add(album);
        AudionAlbumSelect.ALBUM_LIST.link(button, album);

        return button;
    }
}
