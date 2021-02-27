package xkv.visual.panels;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import xkv.visual.css.IStylable;

public class StandardGridPane extends GridPane implements IStylable
{
    public static final int DEFAULT_SPACING = 10;

    protected StandardGridPane()
    {
        setGap(DEFAULT_SPACING);
    }

    protected StandardGridPane(double gap, int columns, int rows)
    {
        setGap(gap);
        pad(columns, rows);
    }

    protected void setGap(double gap)
    {
        this.setHgap(gap);
        this.setVgap(gap);
    }

    protected void pad(int columns, int rows)
    {
        for (int row = 0; row < rows; row += rows - 1)
        {
            for (int col = 0; col < columns; col += columns - 1) {

                Rectangle rect = new Rectangle(0, 0);

                this.add(rect, col, row);
            }
        }
    }

    public void add(Node child, int columnIndex, int rowIndex, Priority... expansionPriorities)
    {
        super.add(child, columnIndex, rowIndex);

        try
        {
            GridPane.setHgrow(child, expansionPriorities[0]);
            GridPane.setVgrow(child, expansionPriorities[1]);
        }
        catch (IndexOutOfBoundsException | NullPointerException ignored)
        {

        }
    }
}
