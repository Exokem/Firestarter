package xkv.visual.panels;

import javafx.scene.shape.Rectangle;
import xkv.visual.controls.MultiButton;
import xkv.visual.css.Style;

public class PanelFactory
{
    /**
     * Provides a {@link StandardGridPane} containing empty {@link Rectangle} objects at each of its corner indices, such that
     * the main window will automatically resize itself to display the entire grid.
     *
     * @param gap The horizontal and vertical gap between grid indices.
     * @param columns The number of columns in the grid.
     * @param rows The number of rows in the grid.
     * @param styles All of the styles that should be applied to the grid.
     */
    public static StandardGridPane paddedGrid(int gap, int columns, int rows, Style... styles)
    {
        StandardGridPane pane = new StandardGridPane(gap, columns, rows);
        pane.addVisualStyle(styles);

        return pane;
    }

    public static StandardScrollPanel<MultiButton> multiButtonScrollPanel()
    {
        return new StandardScrollPanel<>();
    }
}
