package xkv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import xkv.visual.Display;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.AudionPanel;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
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

        rename.setPrefWidth(0.1D * AudionPanel.PANEL_WIDTH);

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
