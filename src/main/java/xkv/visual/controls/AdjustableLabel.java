package xkv.visual.controls;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class AdjustableLabel extends Label
{
    public AdjustableLabel(String text)
    {
        super(text);
    }

    public AdjustableLabel withMinWidth(double width)
    {
        setMinWidth(width);
        return this;
    }

    public AdjustableLabel autoSize()
    {
        setMinWidth(Region.USE_PREF_SIZE);
        return this;
    }
}
