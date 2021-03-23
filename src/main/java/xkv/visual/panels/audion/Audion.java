package xkv.visual.panels.audion;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import xkv.Firestarter;
import xkv.content.Album;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.AdjustableLabel;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.LinkedScrollPane;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;

/**
 * Audion is the main logical container for the Firestarter media player.
 */
public final class Audion
{
    /** The internally accessible New Album Provider. */
    private static final StyledButton NEW_ALBUM_PROVIDER = Components.newAlbumProvider();

    /** The internally accessible Album List. */
    private static final LinkedScrollPane<Button, Album> ALBUM_LIST = Components.albumList();

    /** The externally accessible Audion Panel. */
    public static final StandardGridPane AUDION_PANEL = Components.audionPanel();

    /**
     * Data contains internal values and data structures.
     */
    private static final class Data
    {
        // TODO: read dimensions from config

        /* The width and height of the Audion panel. */
        private static final double PANEL_WIDTH = 1280;
        private static final double PANEL_HEIGHT = 760;

        private static final double MINOR_SCALE_UNIFORM = 0.195D;

        /* The list of all existent albums. */
        private static final List<Album> ALBUMS = new ArrayList<>();
    }

    /**
     * The Scale enum contains enumerated scaling factors that are usable with some scaling functions in order to adjust
     * the width of components based on the dynamic size of the Firestarter program window.
     */
    public enum Scale
    {
        MINOR(0.195D);

        final double factor;

        Scale(double factor)
        {
            this.factor = factor;
        }

        private double factor()
        {
            return factor;
        }

        public double adjust(double factor)
        {
            return this.factor * factor;
        }
    }

    @Deprecated
    public static double scaleMinorWidth()
    {
        return Data.MINOR_SCALE_UNIFORM * Firestarter.width();
    }

    /**
     * Allows external scaling based on the Firestarter window width using enumerated scaling factors for aesthetic
     * consistency.
     *
     * @param scale The {@link Scale} factor used in scaling.
     * @return A double, the window width modified by the provided scale factor.
     */
    public static double scaleWidth(Scale scale)
    {
        return Firestarter.width() * scale.factor();
    }

    /**
     * Allows external scaling based on the Firestarter window width using any scaling factor.
     *
     * @param factor The factor used in scaling.
     * @return A double, the window width modified by the provided factor.
     */
    public static double scaleWidth(double factor)
    {
        return Firestarter.width() * factor;
    }

    /**
     * Allows external scaling based on the Firestarter window height using enumerated scaling factors for aesthetic
     * consistency.
     *
     * @param scale The {@link Scale} factor used in scaling.
     * @return A double, the window height modified by the provided scale factor.
     */
    public static double scaleHeight(Scale scale)
    {
        return Firestarter.height() * scale.factor();
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
        ALBUM_LIST.remove(album);
    }

    public static Button topAlbumButton()
    {
        return ALBUM_LIST.top();
    }

    public static Album albumFromLink(Button link)
    {
        return ALBUM_LIST.getLink(link);
    }

    /**
     * Allows a specific {@link Album} instance to be added to the list of existent albums. Albums that are removed from
     * the list stored in {@link Data} are simultaneously removed from {@link Audion#ALBUM_LIST} so they are also
     * removed from the displayed list.
     *
     * @param album The {@link Album} to be added.
     * @return The button in the displayed album list associated with the newly added album.
     */
    public static StyledButton addAlbum(Album album)
    {
        StyledButton button = new StyledButton(album.displayName());
        button.setOnAction(value -> AudionAlbumView.openAlbum(button, album));

        Data.ALBUMS.add(album);
        ALBUM_LIST.link(button, album);

        return button;
    }

    /**
     * Allows the album creation prompt window to be displayed externally.
     */
    public static void createAlbumPrompt()
    {
        Components.createAlbumPrompt();
    }

    /**
     * Components holds the internal functions used to instantiate various visual components of the Audion Panel.
     */
    private static class Components
    {
        /** Internally produces the {@link LinkedScrollPane} used to display all of the existent albums. */
        private static LinkedScrollPane<Button, Album> albumList()
        {
            LinkedScrollPane<Button, Album> albums = PaneFactory.linkedScrollPane();
            DynamicResizeable.addPrefLimSizeListener(albums, Audion::scaleMinorWidth);

            return albums;
        }

        /** Internally produces the primary {@link StyledButton} used to create new albums. */
        private static StyledButton newAlbumProvider()
        {
            StyledButton button = new StyledButton("New Album").action(Audion::createAlbumPrompt);
            DynamicResizeable.addPrefLimSizeListener(button, Audion::scaleMinorWidth);

            return button;
        }

        /**
         * Internally produces the specific {@link StandardGridPane} layout corresponding to the Audion panel.
         */
        private static StandardGridPane audionPanel()
        {
            StandardGridPane audionPanel = PaneFactory.autoPaddedGrid(10, 2, 3, Style.INSET);

            audionPanel.setPrefWidth(panelWidth());
            audionPanel.setPrefHeight(panelHeight());

            audionPanel.add(NEW_ALBUM_PROVIDER, 1, 1);
            audionPanel.add(ALBUM_LIST, 1, 2, null, Priority.ALWAYS);

            audionPanel.latentAdd(AudionPlayer.View.ICON, 1 ,3);
            audionPanel.latentAdd(AudionPlayer.View.OVERLAY, 1, 3);

            audionPanel.add(AudionAlbumView.albumContentPanel(), 2, 1, 1, 3, Priority.ALWAYS);

            return audionPanel;
        }

        /**
         * Produces a popup window over the main program window that allows the user to create a new album. A created
         * album will be added to the list and display.
         */
        private static void createAlbumPrompt()
        {
            final double altDim = Audion.scaleWidth(Audion.Scale.MINOR.adjust(0.5D));

            StandardGridPane detailContainer = PaneFactory.autoPaddedGrid(10, 2, 6, Style.INSET);
            StandardGridPane contentContainer = PaneFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
            StandardGridPane promptContainer = PaneFactory.autoPaddedGrid(10, 1, 1);

            Stage promptStage = Firestarter.subsidiary(String.format("Album %d", Audion.albumMeasure() + 1), promptContainer);
            StyledImageView imageHolder = new StyledImageView(DEFAULT_IMAGE).resizeByDefault(altDim);

            TextField titleInput = new TextField(promptStage.getTitle());
            titleInput.textProperty().addListener(listener -> promptStage.setTitle(titleInput.getText()));
            titleInput.setPrefWidth(Audion.scaleMinorWidth());

            detailContainer.add(new AdjustableLabel("Title: ").autoSize(), 1, 1);
            detailContainer.add(titleInput, 2, 1, Priority.SOMETIMES);
            detailContainer.alignedH(new StyledButton("Done").width(altDim).action(() ->
            {
                Album album = Album.empty("" + Audion.albumMeasure() + 1).configureImage(imageHolder.getImage());
                album.configureDisplayName(titleInput.getText());

                StyledButton albumButton = addAlbum(album);

                promptStage.close();

                AudionAlbumView.openAlbum(albumButton, album);
            }), 2, 6, HPos.RIGHT);

            contentContainer.add(detailContainer, 2, 1, Priority.ALWAYS);
            contentContainer.add(PaneFactory.styledBorderPane(imageHolder, Style.SMALL_SHADOW), 1, 1);
            contentContainer.add(new StyledButton("Select Image", Style.TRANSPARENT_BUTTON).square(altDim).animate().action(() ->
            {
                Image image = VisualResourceLoader.selectImageDialog(promptStage, "Select an Album Icon");
                imageHolder.setImage(image == null ? imageHolder.getImage() : image);
            }), 1, 1);

            promptContainer.add(contentContainer, 1, 1, Priority.ALWAYS, Priority.ALWAYS);
            promptStage.show();
        }
    }
}
