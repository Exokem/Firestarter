package xkv.visual;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.AudionPanel;


public class Display
{
    private static final StandardGridPane container = PaneFactory.autoPaddedGrid(10, 1, 1);

    public static Pane format()
    {
        return container;
    }

    public static void initialize(Stage stage)
    {
        container.add(AudionPanel.instance(), 1, 1, Priority.ALWAYS, Priority.ALWAYS);
    }
}
