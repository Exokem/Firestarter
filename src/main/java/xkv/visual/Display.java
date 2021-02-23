package xkv.visual;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import xkv.visual.panels.AudionPanel;

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

        applyInset(accessView);


        accessView.add(optionsAccess, 0, 0);
        accessView.add(audionAccess, 1, 0);

        container.setTop(accessView);
        container.setCenter(AudionPanel.instance().inset(10));
    }

    private static final GridPane accessView = new GridPane();

    private static final Button
            audionAccess = new Button("Audion"),
            optionsAccess = new Button("Options");
}
