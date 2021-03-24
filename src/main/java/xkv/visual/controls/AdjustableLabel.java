package xkv.visual.controls;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import xkv.api.controls.IMultiDimensional;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;

public class AdjustableLabel extends Label implements IStylable, IMultiDimensional<AdjustableLabel>
{
    public AdjustableLabel alignment(Pos alignment)
    {
        this.setAlignment(alignment);
        return this;
    }

    public AdjustableLabel autoSize()
    {
        setMinWidth(Region.USE_PREF_SIZE);
        return this;
    }

    public AdjustableLabel pad(double top, double right, double bottom, double left)
    {
        Insets padding = new Insets(top, right, bottom, left);
        setPadding(padding);
        return this;
    }

    public AdjustableLabel(String text)
    {
        super(text);
    }

    public AdjustableLabel(String text, Style... styles)
    {
        this(text);
        addVisualStyle(styles);
    }
}
