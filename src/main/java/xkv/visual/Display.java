package xkv.visual;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import xkv.visual.panels.audion.AudionPanel;

import static xkv.visual.css.PanelApplicators.applyInset;


public class Display
{
    private static final BorderPane container = new BorderPane();

    public static Pane format()
    {
        return container;
    }

    public static void initialize(Stage stage)
    {

        audionAccess.getStyleClass().add("area-button");
        optionsAccess.getStyleClass().add("area-button");

        applyInset(accessView);

//        stage.setResizable(false);

        accessView.add(optionsAccess, 0, 0);
        accessView.add(audionAccess, 1, 0);

        container.setTop(accessView);
        container.setCenter(AudionPanel.instance());
    }

    private static final GridPane accessView = new GridPane();

    private static final Button
            audionAccess = new Button("Audion"),
            optionsAccess = new Button("Options");
}
