package xkv.visual;

import javafx.scene.shape.Rectangle;

public class HiddenRectangle extends Rectangle
{
    public HiddenRectangle(double width, double height)
    {
        super(width, height);
        setVisible(false);
    }
}
