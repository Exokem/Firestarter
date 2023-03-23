package xkv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xkv.data.DataSerializer;
import xkv.data.ResourceLoader;
import xkv.data.VisualResourceLoader;
import xkv.visual.Display;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.Audion;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xkv.data.VisualResourceLoader.loadCSS;

public class Firestarter extends Application
{
    private static class Data
    {
        private static final String TITLE = "Firestarter";
        private static final Logger OUTPUT = Logger.getLogger(TITLE);
        private static final Set<String> STYLESHEETS = new HashSet<>();

        private static Stage FIRESTARTER;
        private static StandardGridPane MAIN_CONTAINER;
    }

    public static double width()
    {
        return Data.FIRESTARTER.getWidth();
    }

    public static double height()
    {
        return Data.FIRESTARTER.getHeight();
    }

    public static void info(String message)
    {
        Data.OUTPUT.log(Level.INFO, message);
    }

    public static void warning(String message)
    {
        Data.OUTPUT.log(Level.WARNING, message);
    }

    public static void severe(String message)
    {
        Data.OUTPUT.log(Level.SEVERE, message);
    }

    public static void importStyleSheet(String convertedURL)
    {
        Data.STYLESHEETS.add(convertedURL);
    }

    public static void importMultiTrackDialog(String message)
    {
        ResourceLoader.importMultiTrackDialog(Data.FIRESTARTER, message);
    }

    public static Image selectImageDialog(String message)
    {
        return VisualResourceLoader.selectImageDialog(Data.FIRESTARTER, message);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        ResourceLoader.SETUP.loadData();

        Data.FIRESTARTER = stage;
        Data.MAIN_CONTAINER = Display.ROOT;

        Scene main = new Scene(Data.MAIN_CONTAINER);

        loadCSS(main, "defaults");
        loadCSS(main, "visual");
        loadCSS(main, "controls");

        DataSerializer.deserializeAlbums();

        stage.setScene(main);
        stage.setTitle(Data.TITLE);

        stage.show();

        main.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(DynamicResizeable::resizeAll));
        main.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(DynamicResizeable::resizeAll));
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
        DataSerializer.serializeAlbums();
    }

    public static Stage subsidiary(String title, Parent root)
    {
        Stage subsidiaryStage = new Stage();
        Scene scene = new Scene(root);

        Data.STYLESHEETS.forEach(sheet -> scene.getStylesheets().add(sheet));

        subsidiaryStage.setTitle(title);
        subsidiaryStage.initOwner(Data.FIRESTARTER);
        subsidiaryStage.setScene(scene);

        subsidiaryStage.initModality(Modality.WINDOW_MODAL);

        return subsidiaryStage;
    }

    @SafeVarargs
    public static Stage renameWindow(String title, String currentName, Consumer<String>... renameFunctions)
    {
        StandardGridPane layout = PaneFactory.autoPaddedGrid(10, 2, 2, Style.INSET);
        StandardGridPane container = PaneFactory.autoPaddedGrid(10, 1, 1);

        Stage subsidiaryStage = subsidiary(title, container);

        container.add(layout, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        Label fieldLabel = new Label(title + ":");
        TextField field = new TextField(currentName);
        StyledButton rename = new StyledButton("Rename");
        GridPane.setHalignment(rename, HPos.RIGHT);

        fieldLabel.setMinWidth(Region.USE_PREF_SIZE);

        rename.setPrefWidth(0.1D * Audion.panelWidth());
        rename.addVisualStyle(Style.UI_BUTTON);

        rename.setOnAction(value ->
        {
            for (Consumer<String> renameFunction : renameFunctions)
            {
                renameFunction.accept(field.getText());
            }

            subsidiaryStage.close();
        });

        layout.add(fieldLabel, 1, 1);
        layout.add(field, 2, 1, Priority.SOMETIMES);
        layout.add(rename, 2, 2);

        return subsidiaryStage;
    }
}
