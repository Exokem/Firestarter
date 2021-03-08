package xkv;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xkv.content.Track;
import xkv.visual.VisualResourceLoader;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.panels.LinkedScrollPane;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.AudionPanel;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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


        @Override
        public String toString()
        {
            return header;
        }
    }

    public static FileChooser.ExtensionFilter AUDIO = new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", ".ogg", "*.aac", "*.m4a");

    public static void importMultiTrackDialog(Stage stage, String title)
    {
        FileChooser fileSelector = new FileChooser();

        fileSelector.setTitle(title);
        fileSelector.getExtensionFilters().add(AUDIO);

        List<File> files = fileSelector.showOpenMultipleDialog(stage);

        StandardGridPane buttons = PaneFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
        StandardGridPane dirOpt = PaneFactory.autoPaddedGrid(10, 2, 4, Style.INSET);
        StandardGridPane adjOpt = PaneFactory.autoPaddedGrid(10, 2, 5, Style.INSET);
        StandardGridPane layout = PaneFactory.autoPaddedGrid(10, 1, 3, Style.INSET);
        StandardGridPane container = PaneFactory.autoPaddedGrid(10, 1, 1);

        Stage importOptions = Firestarter.subsidiary("Import Options", container);

        container.add(layout, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        layout.add(adjOpt, 1, 1, Priority.SOMETIMES);
        layout.add(dirOpt, 1, 2, Priority.SOMETIMES);
        layout.add(buttons, 1, 3, Priority.SOMETIMES);

        Label existing = new Label("Existing Files");
        Label replace = new Label("Replace");
        Label ignore = new Label("Ignore");
        Label adjust = new Label("Adjust");

        replace.setPrefWidth(0.17D * AudionPanel.PANEL_WIDTH);

        ToggleGroup existingGroup = new ToggleGroup();

        RadioButton replaceBox = new RadioButton(), ignoreBox = new RadioButton(), adjustBox = new RadioButton();

        replaceBox.setToggleGroup(existingGroup);
        adjustBox.setToggleGroup(existingGroup);
        ignoreBox.setToggleGroup(existingGroup);
        ignoreBox.setSelected(true);

        adjOpt.add(existing, 1, 1, 2, 1, Priority.SOMETIMES);
        adjOpt.add(new Separator(), 1, 2, 2, 1, Priority.ALWAYS);
        adjOpt.add(replace, 2, 3);
        adjOpt.add(adjust, 2, 4);
        adjOpt.add(ignore, 2, 5, Priority.ALWAYS);

        adjOpt.add(replaceBox, 1, 3);
        adjOpt.add(adjustBox, 1, 4);
        adjOpt.add(ignoreBox, 1, 5);

        Label directory = new Label("Directory");
        Label subDir = new Label("Use Subdirectory");
        CheckBox dirCheck = new CheckBox();
        TextField dirInput = new TextField();

        dirCheck.selectedProperty().addListener((observer, oldVal, newVal) -> dirInput.setDisable(!newVal));

        dirInput.setDisable(true);

        dirOpt.add(directory, 1, 1, 2, 1);
        dirOpt.add(new Separator(), 1, 2, 2, 1);
        dirOpt.add(dirCheck, 1, 3);
        dirOpt.add(subDir, 2, 3, Priority.ALWAYS);
        dirOpt.add(dirInput, 1, 4, 2, 1, Priority.ALWAYS);

        StyledButton cancel = new StyledButton("Cancel"), impor = new StyledButton("Import");
        GridPane.setHalignment(impor, HPos.RIGHT);

        cancel.setMaxWidth(0.05 * AudionPanel.PANEL_WIDTH);
        cancel.setOnAction(value -> importOptions.close());

        impor.setMaxWidth(0.05 * AudionPanel.PANEL_WIDTH);
        impor.setOnAction(value ->
        {
            if (!dirCheck.isSelected() || (dirCheck.isSelected() && !dirInput.getText().isEmpty()))
            {
                ImportMode mode = ImportMode.IGNORE;

                if (adjustBox.isSelected()) mode = ImportMode.ADJUST;
                if (replaceBox.isSelected()) mode = ImportMode.REPLACE;

                String dest = dirCheck.isSelected() ? dirInput.getText() : null;

                importTracks(files, mode, dest);
            }
        });

        buttons.add(cancel, 1, 1, Priority.ALWAYS);
        buttons.add(impor, 2, 1, Priority.ALWAYS);

        importOptions.setResizable(false);
        importOptions.show();
        Platform.runLater(importOptions::sizeToScene);
    }

    private enum ImportMode
    {
        REPLACE, ADJUST, IGNORE
    }

    private static void importTracks(List<File> files, ImportMode mode, String subdirectory)
    {
        String dirPath = EXTERNAL_AUDIO.header;

        if (subdirectory != null)
        {
            if (subdirectory.charAt(subdirectory.length() - 1) != '/')
            {
                subdirectory += '/';
            }

            dirPath += subdirectory;
        }

        List<File> existingFiles = new ArrayList<>();

        List<File> copyFailures = new ArrayList<>();

        for (File file : files)
        {
            String internalPath = dirPath + file.getName();

            File internal = new File(internalPath);

            try
            {
                if (mode == ImportMode.ADJUST)
                {
                    int shift = 1;

                    while (internal.exists())
                    {
                        internal = new File(String.format("%s_%d", internalPath, shift));
                        shift ++;
                    }

                    Files.copy(file.toPath(), internal.toPath());
                }
                else
                {
                    if (mode == ImportMode.REPLACE)
                    {
                        Files.copy(file.toPath(), internal.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    else
                    {
                        Files.copy(file.toPath(), internal.toPath());
                    }
                }

                Track track = Track.fromFile(internal);

                OUTPUT.log(Level.INFO, String.format("Successfully imported %s", internal.getAbsolutePath()));
            }
            catch (FileAlreadyExistsException f)
            {
                OUTPUT.log(Level.WARNING, String.format("Skipping import for %s, file already exists", internal.getAbsolutePath()));
                existingFiles.add(internal);
            }
            catch (IOException e)
            {
                OUTPUT.log(Level.WARNING, String.format("Skipping import for %s, unexpected failure", internal.getAbsolutePath()));
                copyFailures.add(internal);
            }
        }
    }
}
