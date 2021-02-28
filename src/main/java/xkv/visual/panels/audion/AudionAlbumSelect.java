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
import xkv.visual.panels.PanelFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.StandardScrollPanel;

import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;
import static xkv.visual.panels.audion.AudionPanel.addAlbum;
import static xkv.visual.panels.audion.AudionPanel.albums;

public class AudionAlbumSelect
{
    private static final double SCALE = 0.195D;

    protected static final double REFERENCE = 1240;

    protected static StandardScrollPanel<Button> albumList = albumList(REFERENCE);

    private static StandardScrollPanel<Button> albumList(double reference)
    {
        StandardScrollPanel<Button> albums = PanelFactory.buttonScrollPanel().width(SCALE * reference);
        GridPane.setVgrow(albums, Priority.ALWAYS);

        return albums;
    }

    protected static StyledButton albumProvider(double reference)
    {
        StyledButton button = new StyledButton("New Album");

        button.setPrefWidth(SCALE * reference);
        button.setOnAction((actionEvent) -> newAlbum(reference));

        return button;
    }

    protected static void newAlbum(double reference)
    {
        final double altDim = SCALE / 2 * reference;

        /// Grid Cascades
        StandardGridPane promptContainer = PanelFactory.autoPaddedGrid(10, 1, 1);
        StandardGridPane contentContainer = PanelFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
        StandardGridPane detailsContainer = PanelFactory.autoPaddedGrid(10, 2, 6, Style.INSET);

        String defaultTitle = String.format("Album %d", albums.size() + 1);
        Stage promptStage = Firestarter.subsidiary(defaultTitle, promptContainer);

        /// Image Selection
        /// A BorderPane contains the ImageView so that it can be styled.
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
        titleInput.setPrefWidth(SCALE * reference);
        done.setPrefWidth(altDim);
        done.setOnAction(value ->
        {
            Album album = Album.empty(titleInput.getText()).image(imageHolder.getImage());

            addAlbum(album);

            promptStage.close();
        });

        contentContainer.add(imageContainer, 1, 1);
        contentContainer.add(imageSelect, 1, 1);
        contentContainer.add(detailsContainer, 2, 1, Priority.ALWAYS);

//            detailsContainer.addVisualStyle(Style.DEBUG);

        detailsContainer.add(title, 1, 1);
        detailsContainer.add(titleInput, 2, 1, Priority.SOMETIMES);
        detailsContainer.add(done, 2, 6);

        promptContainer.add(contentContainer, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        promptStage.show();
    }
}
