package xkv.visual.panels.audion;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import xkv.Firestarter;
import xkv.ResourceLoader;
import xkv.content.Album;
import xkv.content.Track;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.AdjustableLabel;
import xkv.visual.controls.DynamicButton;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.LinkedScrollPane;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static xkv.ResourceLoader.*;
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

    private static final StandardGridPane EMPTY_ALBUM_VIEW = Components.emptyPanel();

    private static final StandardGridPane ALBUM_VIEW = Components.albumView();

    private static final BorderPane ALBUM_VIEW_HOLDER = new BorderPane(EMPTY_ALBUM_VIEW);

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

        private static final AdjustableLabel ACTIVE_ALBUM_TITLE = new AdjustableLabel("", Style.TITLE_BOLD);
        private static final AdjustableLabel ACTIVE_ALBUM_SPAN = new AdjustableLabel("", Style.SUBTITLE);

        private static Album activeAlbum = null;
        private static Button activeAlbumButton = null;
    }

    /**
     * The Scale enum contains enumerated scaling factors that are usable with some scaling functions in order to adjust
     * the width of components based on the dynamic size of the Firestarter program window.
     */
    public enum Scale
    {
        MINOR(0.195D), MAJOR(0.805);

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
        Data.activeAlbum = null;
        Data.activeAlbumButton = null;
        Data.ALBUMS.remove(album);
        ALBUM_LIST.remove(album);

        if (Data.ALBUMS.isEmpty())
        {
            displayAlbum(null);
        }

        else
        {
            displayAlbum(ALBUM_LIST.top());
        }
    }

    /**
     * Updates the album view to display the album linked to the provided button.
     *
     * @param source The button linked to the album to display.
     */
    public static void displayAlbum(Button source)
    {
        if (source == null)
        {
            ALBUM_VIEW_HOLDER.setCenter(EMPTY_ALBUM_VIEW);
        }

        else
        {
            boolean hasChanged = Data.activeAlbumButton != source;
            Data.activeAlbum = ALBUM_LIST.getLink(source);
            Data.activeAlbumButton = source;

            updateAlbumView(hasChanged, hasChanged);
            ALBUM_VIEW_HOLDER.setCenter(ALBUM_VIEW);
        }
    }

    /**
     * Updates various properties of the album view to reflect changes that may have occurred. The track list for the
     * currently displayed album will only be updated if the album has changed, or the track list has been updated.
     *
     * @param albumChanged Indicates that the displayed album has changed.
     * @param updateList Indicates that the displayed track list should be updated.
     */
    private static void updateAlbumView(boolean albumChanged, boolean updateList)
    {
        if (albumChanged)
        {
            updateList = albumChanged;

            ADN.IMAGE_VIEW.setImage(Data.activeAlbum.image());
            Data.ACTIVE_ALBUM_TITLE.setText(Data.activeAlbum.displayName());
            Data.ACTIVE_ALBUM_SPAN.setText(Data.activeAlbum.formattedSpan());
        }

        if (!updateList) return;

        ADN.TRACK_VIEW.clear();
        Iterator<Track> iterator = Data.activeAlbum.iterator();

        while (iterator.hasNext())
        {
            Track track = iterator.next();
            ADN.TRACK_VIEW.link(track.forDisplay(), track);
        }
    }

    /**
     * Allows a collection of tracks to be imported into the currently displayed album.
     *
     * @param tracks The collection of {@link Track} objects to be imported.
     */
    public static void importTracks(Collection<Track> tracks)
    {
        if (Data.activeAlbum == null) return;

        Data.activeAlbum.addTracks(tracks);
        updateAlbumView(false, true);
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
        button.setOnAction(value -> Audion.displayAlbum(button));

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

            audionPanel.add(ALBUM_VIEW_HOLDER, 2, 1, 1, 3, Priority.ALWAYS);

            return audionPanel;
        }

        private static StandardGridPane emptyPanel()
        {
            Label empty = new Label("There's nothing here");
            GridPane.setHalignment(empty, HPos.CENTER);
            GridPane.setValignment(empty, VPos.BOTTOM);

            empty.setAlignment(Pos.CENTER);
            empty.setMaxWidth(Double.MAX_VALUE);

            StyledButton create = new StyledButton("Create an Album");
            GridPane.setHalignment(create, HPos.CENTER);
            GridPane.setValignment(create, VPos.TOP);

            create.setOnAction(value -> Audion.createAlbumPrompt());
            create.addVisualStyle(Style.UI_BUTTON);

            StandardGridPane emptyPanel = PaneFactory.autoPaddedGrid(10, 1, 2, Style.INSET);

            emptyPanel.add(empty, 1, 1, Priority.ALWAYS, Priority.SOMETIMES);
            emptyPanel.add(create, 1, 2, Priority.ALWAYS, Priority.SOMETIMES);

            return emptyPanel;
        }

        private static StandardGridPane albumView()
        {
            StandardGridPane albumView = PaneFactory.autoPaddedGrid(10, 3, 2, Style.INSET);
            GridPane.setHgrow(albumView, Priority.SOMETIMES);

            BorderPane imageContainer = PaneFactory.styledBorderPane(ResourceLoader.ADN.IMAGE_VIEW, Style.INSET);

            albumView.add(imageContainer, 1, 1);
            albumView.add(albumOptions(), 2, 1);
            albumView.add(albumDetails(), 3, 1, Priority.ALWAYS);
            albumView.add(ResourceLoader.ADN.TRACK_VIEW, 1, 2, 3, 1, Priority.ALWAYS, Priority.ALWAYS);

            return albumView;
        }

        private static StandardGridPane albumDetails()
        {
            StandardGridPane albumDetails = PaneFactory.autoPaddedGrid(10, 1, 2, Style.INSET);

            final Label blank = new Label("");

            GridPane.setValignment(Data.ACTIVE_ALBUM_TITLE, VPos.BOTTOM);
            GridPane.setValignment(Data.ACTIVE_ALBUM_SPAN, VPos.TOP);

            albumDetails.add(Data.ACTIVE_ALBUM_TITLE, 1, 2, Priority.ALWAYS, Priority.SOMETIMES);
            albumDetails.add(Data.ACTIVE_ALBUM_SPAN, 1, 3, Priority.ALWAYS, Priority.SOMETIMES);

            return albumDetails;
        }

        private static StandardGridPane albumOptions()
        {
            DynamicButton deleteIcon = DynamicButton.configure(DEL_ICN, DEL_HOV).configureTooltip("Delete Album");
            DynamicResizeable.addResizeListener(() -> deleteIcon.resize(0.1D * ResourceLoader.ADN.IMAGE_VIEW.getFitHeight()));
            deleteIcon.setOnMouseClicked(value -> removeAlbum(Data.activeAlbum));

            DynamicButton imageSelect = DynamicButton.configure(IMG_ICN, IMG_HOV).configureTooltip("Change Album Image");
            DynamicResizeable.addResizeListener(() -> imageSelect.resize(0.1D * ADN.IMAGE_VIEW.getFitHeight()));

            imageSelect.setOnMouseClicked(value ->
            {
                Image newImage = VisualResourceLoader.selectImageDialog(Firestarter.firestarter, "Select an Album Icon");

                if (newImage != null && Data.activeAlbum != null)
                {
                    Data.activeAlbum.configureImage(newImage);
                    ADN.IMAGE_VIEW.setImage(newImage);
                }
            });

            DynamicButton albumRename = DynamicButton.configure(RNM_ICN, RNM_HOV).configureTooltip("Rename Album");
            DynamicResizeable.addResizeListener(() -> albumRename.resize(0.1D * ADN.IMAGE_VIEW.getFitHeight()));

            albumRename.setOnMouseClicked(value ->
            {
                if (Data.activeAlbum != null)
                {
                    Stage renameStage = Firestarter.renameWindow("Rename Album", Data.activeAlbum.displayName(), Data.activeAlbum::rename, Data.activeAlbumButton::setText, Data.ACTIVE_ALBUM_TITLE::setText);
                    renameStage.show();
                }
            });

            DynamicButton trackImport = DynamicButton.configure(IMT_ICN, IMT_HOV).configureTooltip("Import Tracks");
            DynamicResizeable.addResizeListener(() -> trackImport.resize(0.1D * ADN.IMAGE_VIEW.getFitHeight()));
            trackImport.setOnMouseClicked(value -> ResourceLoader.importMultiTrackDialog(Firestarter.firestarter, "Import Tracks"));

            StandardGridPane albumOptions = PaneFactory.autoPaddedGrid(10, 1, 4, Style.INSET);

            albumOptions.add(deleteIcon, 1, 1);
            albumOptions.add(imageSelect, 1, 2);
            albumOptions.add(albumRename, 1, 3);
            albumOptions.add(trackImport, 1, 4);

            return albumOptions;
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

                Audion.displayAlbum(albumButton);
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
