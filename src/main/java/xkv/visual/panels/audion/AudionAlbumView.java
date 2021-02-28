package xkv.visual.panels.audion;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import xkv.Firestarter;
import xkv.ResourceLoader;
import xkv.content.Album;
import xkv.visual.VisualResourceLoader;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PanelFactory;
import xkv.visual.panels.StandardGridPane;

import static xkv.ResourceLoader.*;
import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;


public class AudionAlbumView
{
    private static ImageView imageHolder = new ImageView(DEFAULT_IMAGE);

    private static Album activeAlbum;

    protected static final StandardGridPane ALBUM_CONTENT_PANEL = albumView();

    protected static final double REFERENCE = AudionPanel.PANEL_WIDTH;

    protected static void openAlbum(Album album)
    {
        if (imageHolder != null && album.image() != null)
        {
            imageHolder.setImage(album.image());
        }

        activeAlbum = album;
    }

    private static StandardGridPane albumOptions()
    {
        StyledImageView deleteIcon = new StyledImageView(DEL_ICN);
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(deleteIcon, 0.1D * imageHolder.getFitHeight()));

        deleteIcon.configureHover(DEL_ICN, DEL_HOV);
        deleteIcon.configureTooltip("Delete Album");

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

        StandardGridPane albumOptions = PanelFactory.autoPaddedGrid(10, 1, 3, Style.INSET);

        albumOptions.add(deleteIcon, 1, 1);
        albumOptions.add(imageSelect, 1, 2);
        albumOptions.add(albumRename, 1, 3);

        return albumOptions;
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
}
