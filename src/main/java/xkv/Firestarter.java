package xkv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xkv.visual.Display;
import xkv.visual.panels.DynamicResizeable;

import java.util.logging.Level;
import java.util.logging.Logger;

import static xkv.visual.VisualResourceLoader.loadCSS;

public class Firestarter extends Application
{
    private static final String TITLE = "Firestarter";

    public static final Logger OUTPUT = Logger.getLogger(TITLE);

    @Override
    public void start(Stage stage) throws Exception
    {
        Display.initialize(stage);

        Scene main = new Scene(Display.format());

        loadCSS(main, "visual");

        stage.setScene(main);
        stage.setTitle(TITLE);

        stage.show();

        main.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(DynamicResizeable::resizeAll));
        main.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(DynamicResizeable::resizeAll));
    }
}
