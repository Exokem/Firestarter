package xkv.visual.controls;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;

public class AdjustableLabel extends Label implements IStylable
{
    public AdjustableLabel(String text)
    {
        super(text);
    }

    public AdjustableLabel(String text, Style... styles)
    {
        this(text);
        addVisualStyle(styles);
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
