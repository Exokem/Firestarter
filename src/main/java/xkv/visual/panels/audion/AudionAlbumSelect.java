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
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.LinkedScrollPane;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;
import static xkv.visual.panels.audion.Audion.addAlbum;

public class AudionAlbumSelect
{
    protected static final double SCALE = 0.195D;

    protected static final double REFERENCE = Audion.panelWidth();

    protected static final LinkedScrollPane<Button, Album> ALBUM_LIST = albumList();

    protected static final Button ALBUM_PROVIDER = albumProvider();

    private static LinkedScrollPane<Button, Album> albumList()
    {
        LinkedScrollPane<Button, Album> albums = PaneFactory.linkedScrollPane().width(SCALE * REFERENCE);
        GridPane.setVgrow(albums, Priority.ALWAYS);
        albums.setMaxWidth(SCALE * REFERENCE);

        DynamicResizeable.addResizeListener(() -> albums.setMaxWidth(SCALE * Firestarter.firestarter.getWidth()));

        return albums;
    }

    private static Button albumProvider()
    {
        StyledButton button = new StyledButton("New Album");

        button.addVisualStyle(Style.UI_BUTTON);
        button.setPrefWidth(SCALE * REFERENCE);
        button.setOnAction((actionEvent) -> newAlbum());

        DynamicResizeable.addResizeListener(() -> button.setPrefWidth(SCALE * Firestarter.firestarter.getWidth()));

        return button;
    }

    /**
     * <p> Opens a new album creation window. </p><br>
     *
     * <h3> Hierarchy </h3>
     *
     * <p> The Prompt Container Overarch contains all first-level components necessary to the album creation display. </p>
     * <p>
     *     The Content Container insets second-level content into the overarch. This includes the image preview and
     *     selection, as well as the entire Detail Container.
     * </p>
     * <p>
     *     The Detail Container contains non-visual user-input components. This includes the album title input, and the
     *     'done' button to finalize album creation.
     * </p>
     */
    protected static void newAlbum()
    {
        final double altDim = SCALE / 2 * REFERENCE;

        StandardGridPane detailContainer = PaneFactory.autoPaddedGrid(10, 2, 6, Style.INSET);
        StandardGridPane contentContainer = PaneFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
        StandardGridPane promptContainer = PaneFactory.autoPaddedGrid(10, 1, 1);

        String defaultTitle = String.format("Album %d", Audion.albumMeasure() + 1);
        Stage promptStage = Firestarter.subsidiary(defaultTitle, promptContainer);

        /// Image Selection

        ImageView imageHolder = new ImageView(DEFAULT_IMAGE);
        BorderPane imageContainer = PaneFactory.styledBorderPane(imageHolder, Style.SMALL_SHADOW);
        VisualResourceLoader.scaleImageView(imageHolder, altDim);
        StyledButton imageSelect = new StyledButton("Select Image").square(altDim).animate();

        imageSelect.addVisualClass("transparent_button");
        imageSelect.setOnAction(value ->
        {
            Image image = VisualResourceLoader.selectImageDialog(promptStage, "Select an Album Icon");
            if (image != null)
            {
                imageHolder.setImage(image);
            }
        });

        Label title = new Label("Title: ");
        TextField titleInput = new TextField(defaultTitle);
        StyledButton done = new StyledButton("Done");
        GridPane.setHalignment(done, HPos.RIGHT);

        title.setMinWidth(Region.USE_PREF_SIZE);
        titleInput.textProperty().addListener(listener -> promptStage.setTitle(titleInput.getText()));
        titleInput.setPrefWidth(SCALE * REFERENCE);
        done.setPrefWidth(altDim);
        done.addVisualStyle(Style.UI_BUTTON);
        done.setOnAction(value ->
        {
            Album album = Album.empty(defaultTitle).configureImage(imageHolder.getImage());
            album.configureDisplayName(titleInput.getText());

            StyledButton albumButton = addAlbum(album);

            promptStage.close();

            AudionAlbumView.openAlbum(albumButton, album);
        });

        /// Grid Cascades

        detailContainer.add(title, 1, 1);
        detailContainer.add(titleInput, 2, 1, Priority.SOMETIMES);
        detailContainer.add(done, 2, 6);

        contentContainer.add(imageContainer, 1, 1);
        contentContainer.add(imageSelect, 1, 1);
        contentContainer.add(detailContainer, 2, 1, Priority.ALWAYS);

        promptContainer.add(contentContainer, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        promptStage.show();
    }
}
