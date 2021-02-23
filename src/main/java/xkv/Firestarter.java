package xkv;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xkv.visual.Display;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xkv.visual.VisualConfiguration.loadCSS;

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
    }


}
