package xkv.visual;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import xkv.Firestarter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class VisualConfiguration
{
    /**
     * Loads a CSS stylesheet into the provided scene. Stylesheet names should not include extensions, and should always
     * refer to files in main/resources/css.
     *
     * @param scene The {@link Scene} that the stylesheet will be applied to.
     * @param sheet The {@link String} name of the CSS file to be loaded.
     */
    public static void loadCSS(Scene scene, String sheet)
    {
        URL url = Firestarter.class.getClassLoader().getResource("css/" + sheet + ".css");

        try
        {
            if (url != null)
            {
                Firestarter.OUTPUT.log(Level.INFO, "Loading stylesheet: " + url.getPath());

                scene.getStylesheets().add(new File(url.getPath()).toURI().toURL().toString());
            }
        } catch (MalformedURLException ignored)
        {

        }
    }
}
