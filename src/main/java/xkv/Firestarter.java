package xkv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xkv.visual.Display;
import xkv.visual.panels.DynamicResizeable;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xkv.visual.VisualResourceLoader.loadCSS;

public class Firestarter extends Application
{
    private static final String TITLE = "Firestarter";

    public static final Logger OUTPUT = Logger.getLogger(TITLE);

    public static final Set<String> STYLESHEETS = new HashSet<>();

    public static Stage firestarter;

    @Override
    public void start(Stage stage) throws Exception
    {
        ResourceLoader.loadResources();

        firestarter = stage;

        Display.initialize(stage);

        Scene main = new Scene(Display.format());

        loadCSS(main, "visual");

        stage.setScene(main);
        stage.setTitle(TITLE);

        stage.show();

        main.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(DynamicResizeable::resizeAll));
        main.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(DynamicResizeable::resizeAll));
    }

    public static Stage subsidiary(String title, Parent root)
    {
        Stage subsidiaryStage = new Stage();
        Scene scene = new Scene(root);

        STYLESHEETS.forEach(sheet -> scene.getStylesheets().add(sheet));

        subsidiaryStage.setTitle(title);
        subsidiaryStage.initOwner(firestarter);
        subsidiaryStage.setScene(scene);

        return subsidiaryStage;
    }
}
