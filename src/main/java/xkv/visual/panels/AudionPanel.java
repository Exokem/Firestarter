package xkv.visual.panels;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.ButtonFactory;
import xkv.visual.controls.MultiButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.PanelApplicators;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;

public class AudionPanel extends BorderPane implements IStylable
{
    private final StandardGridPane layoutContent = PanelFactory.paddedGrid(10, 4, 4, Style.INSET);

    private final DynamicResizeable root;

    public double panelWidth = 1024;
    public double panelHeight = 760;

    private AudionPanel()
    {
        root = DynamicResizeable.root(this, (layoutContent) -> {});

        layoutContent.setPrefWidth(panelWidth);
        layoutContent.setPrefHeight(panelHeight);

        instanceAlbumSelect();

        instanceAlbumView();

        this.setCenter(layoutContent);

//        Platform.runLater(() -> testContent.setVisible(false));
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

        BorderPane imageViewContainer = new BorderPane();
        imageViewContainer.setCenter(display);
        imageViewContainer.getStyleClass().add("shadow");

        albumView.add(imageViewContainer, 1, 1);
    }

    private void instanceAlbumSelect()
    {
        StandardScrollPanel<MultiButton> albums = PanelFactory.multiButtonScrollPanel().width(0.195D * panelWidth);

        for (int indx = 0; indx < 30; indx++)
        {
            MultiButton btn = ButtonFactory.multiButton(0, 2, 2);
//            btn.addVisualStyle(Style.BSP_BUTTON);

            btn.add(new Button("F"), 0, 0);
            btn.add(new Button("X"), 0, 1);

            btn.add(new Button("Album Name"), 1, 0, 1, 2, Priority.ALWAYS, Priority.ALWAYS);

            albums.addItem(btn);
        }

        Button createAlbum = new Button("New Album");

        createAlbum.setPrefWidth(0.195D * panelWidth);

        layoutContent.add(createAlbum, 1, 1);

        layoutContent.add(albums, 1, 2);
        GridPane.setVgrow(albums, Priority.ALWAYS);
    }

    public static AudionPanel instance()
    {
        return new AudionPanel();
    }

    public AudionPanel inset(int dimension)
    {

        BorderSection.edgeSet().forEach(section ->
        {
            Rectangle rect = new Rectangle(dimension, dimension);

            section.place(this, rect);

            rect.setVisible(false);
        });

        return this;
    }
}
