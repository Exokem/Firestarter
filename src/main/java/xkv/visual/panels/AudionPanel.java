package xkv.visual.panels;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import xkv.Firestarter;
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

import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;

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

            for (int indx = 0; indx < 30; indx++)
            {
                MultiButton button = ButtonFactory.multiButton(0, 2, 2);
                button.add(new Button("1"), 1, 1, Priority.SOMETIMES);
                button.add(new Button("2"), 1, 2, Priority.SOMETIMES);
                button.add(new Button("Album"), 2, 1, 1, 2, Priority.ALWAYS, Priority.ALWAYS);

                albums.addItem(button);
            }

            return albums;
        }

        private static StyledButton albumProvider(double reference)
        {
            StyledButton button = new StyledButton("New Album");

            button.setPrefWidth(SCALE * reference);

            button.setOnAction((actionEvent) -> newAlbum(reference));

            return button;
        }

        private static void newAlbum(double reference)
        {
            Stage promptStage = new Stage();
            String defaultTitle = String.format("Album %d", albums.size() + 1);

            StandardGridPane promptContainer = PanelFactory.paddedGrid(10, 3, 3);
            StandardGridPane contentContainer = PanelFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
            StandardGridPane detailsContainer = PanelFactory.autoPaddedGrid(10, 2, 5, Style.INSET);

            Scene main = new Scene(promptContainer);
            Firestarter.STYLESHEETS.forEach(sheet -> main.getStylesheets().add(sheet));

            ImageView imageHolder = new ImageView(DEFAULT_IMAGE);
            BorderPane imageContainer = PanelFactory.styledBorderPane(imageHolder, Style.SMALL_SHADOW);
            VisualResourceLoader.scaleImageView(imageHolder, SCALE / 2 * reference);
            StyledButton imageSelect = new StyledButton("Select Image").square(SCALE / 2 * reference).animate();

            Label title = new Label("Title: ");
            TextField titleInput = new TextField(defaultTitle);

            titleInput.textProperty().addListener(listener -> promptStage.setTitle(titleInput.getText()));
            titleInput.setPrefWidth(SCALE * reference);

            imageSelect.addVisualClass("transparent_button");
            imageSelect.setOnAction(value ->
            {
                Image image = VisualResourceLoader.selectImageDialog(promptStage, "Select an Album Icon");
                if (image != null)
                {
                    imageHolder.setImage(image);
                }
            });

            title.setMinWidth(Region.USE_PREF_SIZE);

            contentContainer.add(imageContainer, 1, 1);
            contentContainer.add(imageSelect, 1, 1);
            contentContainer.add(detailsContainer, 2, 1, Priority.ALWAYS);

            detailsContainer.add(title, 1, 1);
            detailsContainer.add(titleInput, 2, 1, Priority.SOMETIMES);

            promptStage.setTitle(defaultTitle);
            promptContainer.add(contentContainer, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

            promptStage.setScene(main);
            promptStage.show();
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

        Image icon = DEFAULT_IMAGE;

        ImageView display = new ImageView(DEFAULT_IMAGE);

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
