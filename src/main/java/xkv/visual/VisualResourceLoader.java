package xkv.visual;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import xkv.Firestarter;
import xkv.processes.Warning;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class VisualResourceLoader
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

                scene.getStylesheets().add(convertURL(url));
            }
        } catch (MalformedURLException ignored)
        {

        }
    }

    public static Image loadImage(ResourceHeader resourceHeader, String identifier)
    {
        try
        {
            URL sourceURL = Firestarter.class.getClassLoader().getResource(resourceHeader.header + identifier);

            assert sourceURL != null;
            return new Image(convertURL(sourceURL));
        }
        catch (NullPointerException | MalformedURLException | AssertionError exc)
        {
            Firestarter.OUTPUT.log(Level.WARNING, Warning.Header.VRL_ERR + Warning.Content.INVALID_URL);
            return null;
        }
    }

    public static ImageView scaleImageView(ImageView view, double squareFactor)
    {
        view.setFitHeight(squareFactor);
        view.setFitWidth(squareFactor);

        return view;
    }

    public static String convertURL(URL url) throws MalformedURLException
    {
        return new File(url.getPath()).toURI().toURL().toString();
    }

    public static enum ResourceHeader
    {
        ALBUM_ICONS("icon/album/");

        String header;

        ResourceHeader(String header)
        {
            this.header = header;
        }
    }
}
