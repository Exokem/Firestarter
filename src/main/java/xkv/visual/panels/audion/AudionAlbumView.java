package xkv.visual.panels.audion;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import xkv.Firestarter;
import xkv.ResourceLoader;
import xkv.content.Album;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PanelFactory;
import xkv.visual.panels.StandardGridPane;

import static xkv.ResourceLoader.*;


public class AudionAlbumView
{
    private static final ImageView imageHolder = new ImageView();

    private static Album activeAlbum;
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
        if (album.image() != null)
        {
            imageHolder.setImage(album.image());
        }

        albumButton = source;
        activeAlbum = album;

        ALBUM_CONTENT_PANEL.setCenter(ALBUM_VIEW);
    }

    protected static void deleteAlbum(Album album)
    {
        AudionPanel.albums.remove(album);

        AudionAlbumSelect.ALBUM_LIST.remove(album);

        activeAlbum = null;
        albumButton = null;
        imageHolder.setImage(null);

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

        create.setOnAction(value ->
        {
            AudionAlbumSelect.newAlbum();
        });

        StandardGridPane emptyPanel = PanelFactory.autoPaddedGrid(10, 1, 2, Style.INSET);

        emptyPanel.add(empty, 1, 1, Priority.ALWAYS, Priority.SOMETIMES);
        emptyPanel.add(create, 1, 2, Priority.ALWAYS, Priority.SOMETIMES);

        return emptyPanel;
    }

    private static StandardGridPane albumView()
    {
        StandardGridPane albumView = PanelFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
        GridPane.setHgrow(albumView, Priority.SOMETIMES);

        VisualResourceLoader.scaleImageView(imageHolder, 0.20D * REFERENCE);
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(imageHolder, 0.20D * AudionPanel.CONTENT_OVERARCH.getWidth()));

        imageHolder.maxWidth(albumView.getWidth());
        imageHolder.maxHeight(albumView.getHeight());

        BorderPane imageContainer = PanelFactory.styledBorderPane(imageHolder);

        albumView.add(imageContainer, 1, 1);
        albumView.add(albumOptions(), 2, 1);

        return albumView;
    }

    private static StandardGridPane albumOptions()
    {
        StyledImageView deleteIcon = new StyledImageView(DEL_ICN);
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(deleteIcon, 0.1D * imageHolder.getFitHeight()));

        deleteIcon.configureHover(DEL_ICN, DEL_HOV);
        deleteIcon.configureTooltip("Delete Album");

        deleteIcon.setOnMouseClicked(value ->
        {
            deleteAlbum(activeAlbum);
        });

        StyledImageView imageSelect = new StyledImageView(IMG_ICN);
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(imageSelect, 0.1D * imageHolder.getFitHeight()));

        imageSelect.configureHover(IMG_ICN, IMG_HOV);
        imageSelect.configureTooltip("Change Album Image");

        imageSelect.setOnMouseClicked(value ->
        {
            Image newImage = VisualResourceLoader.selectImageDialog(Firestarter.firestarter, "Select an Album Icon");

            if (newImage != null && activeAlbum != null)
            {
                activeAlbum.configureImage(newImage);
                imageHolder.setImage(newImage);
            }
        });

        StyledImageView albumRename = new StyledImageView(ResourceLoader.RNM_ICN);
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(albumRename, 0.1D * imageHolder.getFitHeight()));

        albumRename.configureHover(RNM_ICN, RNM_HOV);
        albumRename.configureTooltip("Rename Album");

        albumRename.setOnMouseClicked(value ->
        {
            if (activeAlbum != null)
            {
                Stage renameStage = Firestarter.renameWindow("Rename Album", activeAlbum.displayName(), activeAlbum::rename, albumButton::setText);
                renameStage.show();
            }
        });

        StandardGridPane albumOptions = PanelFactory.autoPaddedGrid(10, 1, 3, Style.INSET);

        albumOptions.add(deleteIcon, 1, 1);
        albumOptions.add(imageSelect, 1, 2);
        albumOptions.add(albumRename, 1, 3);

        return albumOptions;
    }
}
