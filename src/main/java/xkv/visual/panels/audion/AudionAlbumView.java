package xkv.visual.panels.audion;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import xkv.visual.controls.DynamicButton;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import java.util.Collection;
import java.util.Iterator;

import static xkv.ResourceLoader.*;


public class AudionAlbumView
{
    protected static Album activeAlbum;
    private static Button albumButton;

    private static final StandardGridPane EMPTY_PANEL = emptyPanel();
    private static final StandardGridPane ALBUM_VIEW = albumView();

    private static final BorderPane ALBUM_CONTENT_PANEL = new BorderPane(EMPTY_PANEL);

    protected static final double REFERENCE = AudionPanel.PANEL_WIDTH;

    protected static BorderPane albumContentPanel()
    {
        openFirstAlbum();

        return ALBUM_CONTENT_PANEL;
    }

    protected static void openFirstAlbum()
    {
        if (!AudionPanel.albums.isEmpty())
        {
            openAlbum(AudionAlbumSelect.ALBUM_LIST.top());
            ALBUM_CONTENT_PANEL.setCenter(ALBUM_VIEW);
        }
        else
        {
            AudionPanel.createdAlbums = 0;
            ALBUM_CONTENT_PANEL.setCenter(EMPTY_PANEL);
        }
    }

    protected static void openAlbum(Button source)
    {
        openAlbum(source, AudionAlbumSelect.ALBUM_LIST.getLink(source));
    }

    protected static void openAlbum(Button source, Album album)
    {
        ADN.IMAGE_VIEW.setImage(album.image());

        albumButton = source;
        activeAlbum = album;

        ALBUM_CONTENT_PANEL.setCenter(ALBUM_VIEW);

        updateAlbumDetails();
    }

    protected static void deleteAlbum(Album album)
    {
        AudionPanel.albums.remove(album);

        AudionAlbumSelect.ALBUM_LIST.remove(album);

        activeAlbum = null;
        albumButton = null;
        ADN.IMAGE_VIEW.setImage(null);

        openFirstAlbum();
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

        create.setOnAction(value -> AudionAlbumSelect.newAlbum());
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

        VisualResourceLoader.scaleImageView(ADN.IMAGE_VIEW, 0.20D * REFERENCE);
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(ADN.IMAGE_VIEW, 0.20D * AudionPanel.CONTENT_OVERARCH.getWidth()));

        ADN.IMAGE_VIEW.maxWidth(albumView.getWidth());
        ADN.IMAGE_VIEW.maxHeight(albumView.getHeight());

        BorderPane imageContainer = PaneFactory.styledBorderPane(ADN.IMAGE_VIEW, Style.INSET);

        albumView.add(imageContainer, 1, 1);
        albumView.add(albumOptions(), 2, 1);
        albumView.add(albumDetails(), 3, 1, Priority.ALWAYS);
        albumView.add(ADN.TRACK_VIEW, 1, 2, 3, 1, Priority.ALWAYS, Priority.ALWAYS);

        return albumView;
    }

    public static void refresh()
    {
        updateAlbumDetails();
    }

    public static void importTracks(Collection<Track> tracks, boolean intoLibrary)
    {
        if (activeAlbum != null)
        {
            activeAlbum.addTracks(tracks);
        }

        if (intoLibrary)
        {
            if (activeAlbum != AudionPanel.LIBRARY)
            {
                AudionPanel.LIBRARY.addTracks(tracks);
            }
        }
    }

    private static Label albumTitle, albumSpan;

    private static void updateAlbumDetails()
    {
        if (activeAlbum != null)
        {
            albumTitle.setText(activeAlbum.displayName());
            albumSpan.setText(activeAlbum.formattedSpan());

            ADN.TRACK_VIEW.clear();

            Iterator<Track> iterator = activeAlbum.iterator();

            while (iterator.hasNext())
            {
                Track track = iterator.next();
                ADN.TRACK_VIEW.link(track.forDisplay(), track);
            }
        }
    }

    private static StandardGridPane albumDetails()
    {
        StandardGridPane albumDetails = PaneFactory.autoPaddedGrid(10, 1, 3, Style.INSET);

        Label blank = new Label("");
        albumTitle = new Label("Test Title");
        albumSpan = new Label("Test Span");

        GridPane.setValignment(blank, VPos.BOTTOM);
        GridPane.setValignment(albumSpan, VPos.TOP);

        albumDetails.add(blank, 1, 1, Priority.ALWAYS, Priority.SOMETIMES);
        albumDetails.add(albumTitle, 1, 2, Priority.ALWAYS);
        albumDetails.add(albumSpan, 1, 3, Priority.ALWAYS, Priority.SOMETIMES);

        Style.apply(albumTitle, Style.TITLE_BOLD);
        Style.apply(albumSpan, Style.SUBTITLE);

        return albumDetails;
    }

    private static StandardGridPane albumOptions()
    {
        DynamicButton deleteIcon = DynamicButton.configure(DEL_ICN, DEL_HOV).configureTooltip("Delete Album");
        DynamicResizeable.addResizeListener(() -> deleteIcon.resize(0.1D * ADN.IMAGE_VIEW.getFitHeight()));
        deleteIcon.setOnMouseClicked(value -> deleteAlbum(activeAlbum));

        DynamicButton imageSelect = DynamicButton.configure(IMG_ICN, IMG_HOV).configureTooltip("Change Album Image");
        DynamicResizeable.addResizeListener(() -> imageSelect.resize(0.1D * ADN.IMAGE_VIEW.getFitHeight()));

        imageSelect.setOnMouseClicked(value ->
        {
            Image newImage = VisualResourceLoader.selectImageDialog(Firestarter.firestarter, "Select an Album Icon");

            if (newImage != null && activeAlbum != null)
            {
                activeAlbum.configureImage(newImage);
                ADN.IMAGE_VIEW.setImage(newImage);
            }
        });

        DynamicButton albumRename = DynamicButton.configure(RNM_ICN, RNM_HOV).configureTooltip("Rename Album");
        DynamicResizeable.addResizeListener(() -> albumRename.resize(0.1D * ADN.IMAGE_VIEW.getFitHeight()));

        albumRename.setOnMouseClicked(value ->
        {
            if (activeAlbum != null)
            {
                Stage renameStage = Firestarter.renameWindow("Rename Album", activeAlbum.displayName(), activeAlbum::rename, albumButton::setText, albumTitle::setText);
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
}
