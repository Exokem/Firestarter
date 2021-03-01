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
import xkv.visual.panels.LinkedScrollPane;
import xkv.visual.panels.PanelFactory;
import xkv.visual.panels.StandardGridPane;

import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;
import static xkv.visual.panels.audion.AudionPanel.addAlbum;
import static xkv.visual.panels.audion.AudionPanel.albums;

public class AudionAlbumSelect
{
    private static final double SCALE = 0.195D;

    protected static final double REFERENCE = AudionPanel.PANEL_WIDTH;

    protected static final LinkedScrollPane<Button, Album> ALBUM_LIST = albumList();

    private static LinkedScrollPane<Button, Album> albumList()
    {
        LinkedScrollPane<Button, Album> albums = PanelFactory.linkedScrollPane().width(SCALE * REFERENCE);
        GridPane.setVgrow(albums, Priority.ALWAYS);

        return albums;
    }

    protected static StyledButton albumProvider()
    {
        StyledButton button = new StyledButton("New Album");

        button.setPrefWidth(SCALE * REFERENCE);
        button.setOnAction((actionEvent) -> newAlbum());

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

        StandardGridPane detailContainer = PanelFactory.autoPaddedGrid(10, 2, 6, Style.INSET);
        StandardGridPane contentContainer = PanelFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
        StandardGridPane promptContainer = PanelFactory.autoPaddedGrid(10, 1, 1);

        String defaultTitle = String.format("Album %d", albums.size() + 1);
        Stage promptStage = Firestarter.subsidiary(defaultTitle, promptContainer);

        /// Image Selection

        ImageView imageHolder = new ImageView(DEFAULT_IMAGE);
        BorderPane imageContainer = PanelFactory.styledBorderPane(imageHolder, Style.SMALL_SHADOW);
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
        done.setOnAction(value ->
        {
            Album album = Album.empty(titleInput.getText()).configureImage(imageHolder.getImage());

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
