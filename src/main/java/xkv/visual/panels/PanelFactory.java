package xkv.visual.panels;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import xkv.content.Album;
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
    @Deprecated public static StandardGridPane paddedGrid(int gap, int columns, int rows, Style... styles)
    {
        StandardGridPane pane = new StandardGridPane(gap, columns, rows);
        pane.addVisualStyle(styles);

        return pane;
    }

    public static StandardGridPane autoPaddedGrid(int gap, int innerColumns, int innerRows, Style... styles)
    {
        StandardGridPane pane = new StandardGridPane(gap, innerColumns + 2, innerRows + 2);
        pane.addVisualStyle(styles);

        return pane;
    }

    public static LinkedScrollPane<Button, Album> linkedScrollPane(Style... styles)
    {
        LinkedScrollPane<Button, Album> scrollPane = new LinkedScrollPane<>();
        scrollPane.addVisualStyle(styles);
        return scrollPane;
    }

    public static StandardScrollPanel<Button> buttonScrollPanel(Style... styles)
    {
        StandardScrollPanel<Button> panel = new StandardScrollPanel<>();
        panel.addVisualStyle(styles);
        return panel;
    }

    public static StandardScrollPanel<MultiButton> multiButtonScrollPanel()
    {
        return new StandardScrollPanel<>();
    }

    public static BorderPane styledBorderPane(Node center, Style... styles)
    {
        BorderPane pane = new BorderPane(center);

        for (Style style : styles)
        {
            pane.getStyleClass().add(style.style());
        }

        return pane;
    }
}
