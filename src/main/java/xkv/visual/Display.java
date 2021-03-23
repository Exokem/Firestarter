package xkv.visual;

import javafx.scene.layout.Priority;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.Audion;


public class Display
{
    public static final StandardGridPane ROOT = root();

    private static StandardGridPane root()
    {
        StandardGridPane root = PaneFactory.autoPaddedGrid(10, 1, 1);
        root.add(Audion.AUDION_PANEL, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        return root;
    }
}
