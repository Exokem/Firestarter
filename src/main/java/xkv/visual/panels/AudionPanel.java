package xkv.visual.panels;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import xkv.content.Album;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.ButtonFactory;
import xkv.visual.controls.MultiButton;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.PanelApplicators;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;

import java.util.ArrayList;
import java.util.List;

public class AudionPanel extends StandardGridPane implements IStylable
{
    public static final List<Album> albums = new ArrayList<>();

    public static AudionPanel instance()
    {
        return new AudionPanel();
    }

    private final StandardGridPane layoutContent = PanelFactory.paddedGrid(10, 4, 4, Style.INSET);

    private final DynamicResizeable root;

    private final double panelWidth = 1240;
    private final double panelHeight = 760;

    private AudionPanel()
    {
        super();

        this.pad(3, 3);

        root = DynamicResizeable.root(this, (layoutContent) -> {});

        GridPane.setHgrow(layoutContent, Priority.ALWAYS);
        GridPane.setVgrow(layoutContent, Priority.ALWAYS);

        layoutContent.setPrefWidth(panelWidth);
        layoutContent.setPrefHeight(panelHeight);

        layoutContent.add(AudionAlbumSelect.albumProvider(panelWidth), 1, 1);
        layoutContent.add(AudionAlbumSelect.albumView(panelWidth), 1, 2);

        instanceAlbumView();

        this.add(layoutContent, 1, 1);
    }

    private double scaledWidth(double scale)
    {
        return layoutContent.getWidth() * scale;
    }

    private static class AudionAlbumSelect
    {
        private static final double SCALE = 0.195D;

        private static StandardScrollPanel<MultiButton> albumView(double reference)
        {
            StandardScrollPanel<MultiButton> albums = PanelFactory.multiButtonScrollPanel().width(SCALE * reference);
            GridPane.setVgrow(albums, Priority.ALWAYS);

            return albums;
        }

        private static StyledButton albumProvider(double reference)
        {
            StyledButton button = new StyledButton("New Album");

            button.setPrefWidth(SCALE * reference);

            button.setOnAction((actionEvent) -> {});

            return button;
        }
    }

    private class AudionAlbumView
    {
        // TODO: refactor final layout to here
    }

    private void instanceAlbumView()
    {
        StandardGridPane albumView = PanelFactory.paddedGrid(10, 3, 3, Style.INSET);

        GridPane.setHgrow(albumView, Priority.SOMETIMES);

        layoutContent.add(albumView, 2, 1, 1, 2);

        Image icon = VisualResourceLoader.loadImage(VisualResourceLoader.ResourceHeader.ALBUM_ICONS, "test2.png");

        StyledImageView display = new StyledImageView(icon);

        display.addVisualStyle();

        display.maxWidth(albumView.getWidth());
        display.maxHeight(albumView.getHeight());

        display.addVisualStyle(Style.SHADOW);

        if (icon != null)
        {
            VisualResourceLoader.scaleImageView(display, 0.20D * panelWidth);
            display.setImage(icon);
        }

        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(display, 0.20D * layoutContent.getWidth()));

        BorderPane imageViewContainer = new BorderPane();
        imageViewContainer.setCenter(display);
        imageViewContainer.getStyleClass().add("shadow");

        albumView.add(imageViewContainer, 1, 1);
    }
}
