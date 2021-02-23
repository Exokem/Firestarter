package xkv.visual.controls;

import javafx.scene.control.ScrollPane;

public class ButtonScrollPane extends ScrollPane
{
    private ButtonScrollPane()
    {

    }

    public static ButtonScrollPane instance()
    {
        return new ButtonScrollPane();
    }
}
