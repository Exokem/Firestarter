package xkv;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xkv.content.Track;
import xkv.visual.VisualResourceLoader;
import xkv.visual.css.Style;
import xkv.visual.panels.LinkedScrollPane;
import xkv.visual.panels.StandardGridPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static xkv.Firestarter.OUTPUT;
import static xkv.ResourceLoader.ResourceHeader.EXTERNAL_AUDIO;
import static xkv.ResourceLoader.ResourceHeader.UI_BUTTONS;

public class ResourceLoader
{
    private static final String DATA = "./data/";

    protected static void loadResources()
    {

    }

    public static final Image TRK_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "play_button.png");
    public static final Image TRK_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "play_button_hover.png");

    public static final Image DEL_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "x_button.png");
    public static final Image DEL_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "x_button_hover.png");

    public static final Image IMG_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "image_button.png");
    public static final Image IMG_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "image_button_hover.png");

    public static final Image RNM_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "rename.png");
    public static final Image RNM_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "rename_hover.png");

    public static final Image IMT_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "import_track.png");
    public static final Image IMT_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "import_track_hover.png");

    public static final class SETUP
    {
        protected static void loadData()
        {
            File dataDir = new File(DATA);
            createDir(dataDir);

            File audio = new File(EXTERNAL_AUDIO.header);
            createDir(audio);
        }

        private static boolean createDir(ResourceHeader header)
        {
            return createDir(new File(header.header));
        }

        private static boolean createDir(File file)
        {
            if (!file.exists())
            {
                if (!file.mkdir())
                {
                    OUTPUT.log(Level.SEVERE, "Critical initialization failure: missing data directory could not be created");
                    System.exit(-999);
                }
            }

            if (!file.isDirectory())
            {
                OUTPUT.log(Level.SEVERE, "Critical initialization failure: invalid data directory");
                System.exit(-998);
            }

            return true;
        }
    }

    public static final class ADN
    {
        public static final ImageView IMAGE_VIEW = new ImageView();

        public static final LinkedScrollPane<StandardGridPane, Track> TRACK_VIEW = new LinkedScrollPane<>(Style.INSET);
    }

    public enum ResourceHeader
    {
        ALBUM_ICONS("icon/album/"), UI_BUTTONS("assets/ui/buttons/"),
        EXTERNAL_AUDIO(DATA + "audio/");

        String header;

        public String header()
        {
            return header;
        }

        ResourceHeader(String header)
        {
            this.header = header;
        }
    }

    public static FileChooser.ExtensionFilter AUDIO = new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", ".ogg", "*.aac");

    public static List<String> importMultiTrackDialog(Stage stage, String title)
    {
        FileChooser fileSelector = new FileChooser();

        fileSelector.setTitle(title);
        fileSelector.getExtensionFilters().add(AUDIO);

        List<File> files = fileSelector.showOpenMultipleDialog(stage);

        List<String> paths = new ArrayList<>();

        return null;
    }
}
