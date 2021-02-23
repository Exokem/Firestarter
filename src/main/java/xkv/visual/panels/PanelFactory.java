package xkv.visual.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import xkv.visual.css.PanelStyle;

import static xkv.visual.css.PanelApplicators.applyInset;

public class PanelFactory
{
    public static GridPane styledGrid(int gap, PanelStyle... styles)
    {
        GridPane pane = new GridPane();

        for (PanelStyle style : styles)
        {
            pane.getStyleClass().add(style.style());
        }

        pane.setHgap(gap);
        pane.setVgap(gap);

        return pane;
    }

    /**
     * Provides a {@link GridPane} containing empty {@link Rectangle} objects at each of its corner indices, such that
     * the main window will automatically resize itself to display the entire grid.
     *
     * @param gap The horizontal and vertical gap between grid indices.
     * @param width The number of columns in the grid.
     * @param height The number of rows in the grid.
     * @param styles All of the styles that should be applied to the grid.
     */
    public static GridPane paddedGrid(int gap, int width, int height, PanelStyle... styles)
    {
        GridPane pane = styledGrid(gap, styles);

        for (int row = 0; row < height; row += height - 1)
        {
            for (int col = 0; col < width; col += width - 1) {

                Rectangle rect = new Rectangle(0, 0);

                pane.add(rect, col, row);
            }
        }

        return pane;
    }
}
