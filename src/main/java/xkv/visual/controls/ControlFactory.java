package xkv.visual.controls;

import xkv.visual.css.Style;

public class ControlFactory
{
    public static MultiButton multiButton(int gap, int columns, int rows, Style... styles)
    {
        MultiButton button = new MultiButton(gap, columns, rows);
        button.addVisualStyle(styles);
        return button;
    }
}
