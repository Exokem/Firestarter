package xkv.visual.panels.audion;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import xkv.Firestarter;
import xkv.content.Album;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PanelFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.StandardScrollPanel;

import java.util.ArrayList;
import java.util.List;

import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;

public class AudionPanel extends StandardGridPane implements IStylable
{
    public static AudionPanel instance()
    {
        return new AudionPanel();
    }

    private final StandardGridPane layoutContent = PanelFactory.paddedGrid(10, 4, 4, Style.INSET);

    private final DynamicResizeable root;

    private static final double panelWidth = 1240;
    private static final double panelHeight = 760;

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
        layoutContent.add(AudionAlbumSelect.albumList, 1, 2);

        instanceAlbumView();

        this.add(layoutContent, 1, 1);
    }

    protected static final List<Album> albums = new ArrayList<>();

    protected static void addAlbum(Album album)
    {
        StyledButton button = new StyledButton(album.identifier());

        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(panelHeight / 20);
        button.setOnAction(value -> AudionAlbumView.openAlbum(album));

        AudionAlbumSelect.albumList.addItem(button);
        albums.add(album);
    }

    protected double scaledWidth(double scale)
    {
        return layoutContent.getWidth() * scale;
    }

    private void instanceAlbumView()
    {
        StandardGridPane albumView = PanelFactory.autoPaddedGrid(10, 1, 1, Style.INSET);

        GridPane.setHgrow(albumView, Priority.SOMETIMES);

        layoutContent.add(albumView, 2, 1, 1, 2);

        Image icon = DEFAULT_IMAGE;

        ImageView display = new ImageView(DEFAULT_IMAGE);

        AudionAlbumView.openAlbumImageView = display;

        display.maxWidth(albumView.getWidth());
        display.maxHeight(albumView.getHeight());

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
