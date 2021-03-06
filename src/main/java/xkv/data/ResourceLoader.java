package xkv.data;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xkv.Firestarter;
import xkv.content.Track;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.images.StyledImageView;
import xkv.visual.panels.LinkedScrollPane;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.Audion;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static xkv.Firestarter.*;
import static xkv.data.ResourceLoader.ResourceHeader.EXTERNAL_AUDIO;
import static xkv.data.ResourceLoader.ResourceHeader.UI_BUTTONS;

public class ResourceLoader
{
    private static final String DATA = "./data/";

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

    public static final Image PLA_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "player_play.png");
    public static final Image PLA_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "player_play_hover.png");

    public static final Image PAU_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "player_pause.png");
    public static final Image PAU_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "player_pause_hover.png");

    public static final Image NXT_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "player_next.png");
    public static final Image NXT_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "player_next_hover.png");

    public static final Image PRE_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "player_prev.png");
    public static final Image PRE_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "player_prev_hover.png");

    public static final Image RND_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "player_rand.png");
    public static final Image RND_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "player_rand_hover.png");
    public static final Image RND_ONI = VisualResourceLoader.loadImage(UI_BUTTONS, "player_rand_on.png");
    public static final Image RND_ONH = VisualResourceLoader.loadImage(UI_BUTTONS, "player_rand_on_hover.png");

    public static final Image RPT_ICN = VisualResourceLoader.loadImage(UI_BUTTONS, "player_loop.png");
    public static final Image RPT_HOV = VisualResourceLoader.loadImage(UI_BUTTONS, "player_loop_hover.png");
    public static final Image RPT_ONI = VisualResourceLoader.loadImage(UI_BUTTONS, "player_loop_on.png");
    public static final Image RPT_ONH = VisualResourceLoader.loadImage(UI_BUTTONS, "player_loop_on_hover.png");

    public static final class SETUP
    {
        public static void loadData()
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
                    severe("Critical initialization failure: missing data directory could not be created");
                    System.exit(-999);
                }
            }

            if (!file.isDirectory())
            {
                severe("Critical initialization failure: invalid data directory");
                System.exit(-998);
            }

            return true;
        }
    }

    public static final class ADN
    {
        public static final StyledImageView IMAGE_VIEW = new StyledImageView(null).autoResize(() -> Audion.scaleWidth(0.2D));

        public static final LinkedScrollPane<StandardGridPane, Track> TRACK_VIEW = new LinkedScrollPane<>(Style.INSET);
    }

    public enum ResourceHeader
    {
        ALBUM_ICONS("icon/album/"), UI_BUTTONS("assets/ui/buttons/"),
        EXTERNAL_AUDIO(DATA + "audio/"), ALBUM_DATA(DATA + "albums/");

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

    public static final CheckBox INTO_LIBRARY = new CheckBox();

    public static void importMultiTrackDialog(Stage stage, String title)
    {
        FileChooser fileSelector = new FileChooser();

        fileSelector.setTitle(title);
        fileSelector.getExtensionFilters().add(AUDIO);

        List<File> files = fileSelector.showOpenMultipleDialog(stage);

        if (files == null) return;

        StandardGridPane buttons = PaneFactory.autoPaddedGrid(10, 2, 1, Style.INSET);
        StandardGridPane misc = PaneFactory.autoPaddedGrid(10, 2, 3, Style.INSET);
        StandardGridPane dirOpt = PaneFactory.autoPaddedGrid(10, 2, 4, Style.INSET);
        StandardGridPane adjOpt = PaneFactory.autoPaddedGrid(10, 2, 5, Style.INSET);
        StandardGridPane layout = PaneFactory.autoPaddedGrid(10, 1, 4, Style.INSET);
        StandardGridPane container = PaneFactory.autoPaddedGrid(10, 1, 1);

        Stage importOptions = Firestarter.subsidiary("Import Options", container);

        container.add(layout, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        layout.add(adjOpt, 1, 1, Priority.SOMETIMES);
        layout.add(dirOpt, 1, 2, Priority.SOMETIMES);
        layout.add(misc, 1, 3, Priority.SOMETIMES);
        layout.add(buttons, 1, 4, Priority.SOMETIMES);

        Label existing = new Label("Existing Files");
        Label replace = new Label("Replace");
        Label ignore = new Label("Ignore");
        Label adjust = new Label("Adjust");

        replace.setPrefWidth(0.17D * Audion.panelWidth());

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

        Label general = new Label("General");
        Label library = new Label("Import to Library");

        INTO_LIBRARY.setSelected(true);

        misc.add(general, 1, 1, 2, 1);
        misc.add(new Separator(), 1, 2, 2, 1);
        misc.add(INTO_LIBRARY, 1, 3);
        misc.add(library, 2, 3, Priority.ALWAYS);

        StyledButton cancel = new StyledButton("Cancel"), impor = new StyledButton("Import");
        GridPane.setHalignment(impor, HPos.RIGHT);

        cancel.setMaxWidth(0.05 * Audion.panelWidth());
        cancel.setOnAction(value -> importOptions.close());

        impor.setMaxWidth(0.05 * Audion.panelWidth());
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

        File directory = new File(dirPath);

        if (!directory.exists())
        {
            if (!directory.mkdir())
            {
                warning(String.format("Import failure; directory %s could not be created", dirPath));
                return;
            }
        }

        List<File> existingFiles = new ArrayList<>();

        List<File> copyFailures = new ArrayList<>();

        final List<Track> importedTracks = new ArrayList<>();

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

                importedTracks.add(Track.fromFile(internal));

                info(String.format("Successfully imported %s", internal.getAbsolutePath()));
            }
            catch (FileAlreadyExistsException f)
            {
                warning(String.format("Skipping import for %s, file already exists", internal.getAbsolutePath()));
                existingFiles.add(internal);
            }
            catch (IOException e)
            {
                warning(String.format("Skipping import for %s, unexpected failure", internal.getAbsolutePath()));
                copyFailures.add(internal);
            }
        }

        Audion.importTracks(importedTracks);
    }
}
