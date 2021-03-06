package xkv.visual.panels;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import xkv.visual.controls.StyledButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;

public class StandardGridPane extends GridPane implements IStylable
{
    public static final int DEFAULT_SPACING = 10;

    public StandardGridPane(Style... styles)
    {
        this.addVisualStyle(styles);
    }

    protected StandardGridPane(double gap, int columns, int rows)
    {
        setGap(gap);
        pad(columns, rows);
        setSnapToPixel(true);
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

    private void applyPriorities(Node child, Priority... expansionPriorities)
    {
        try
        {
            GridPane.setHgrow(child, expansionPriorities[0]);
            GridPane.setVgrow(child, expansionPriorities[1]);
        }
        catch (IndexOutOfBoundsException | NullPointerException ignored)
        {
        }
    }

    public void latentAdd(Node child, int col, int row, int colspan, int rowspan, Priority... expansionPriorities)
    {
        Platform.runLater(() -> add(child, col, row, colspan, rowspan, expansionPriorities));
    }

    public void latentAdd(Node child, int col, int row, Priority... expansionPriorities)
    {
        Platform.runLater(() -> add(child, col, row, expansionPriorities));
    }

    public void alignedH(Node child, int col, int row, HPos alignment, Priority... expansionPriorities)
    {
        add(child, col, row, expansionPriorities);
        GridPane.setHalignment(child, alignment);
    }

    public void add(Node child, int columnIndex, int rowIndex, Priority... expansionPriorities)
    {
        super.add(child, columnIndex, rowIndex);
        applyPriorities(child, expansionPriorities);
    }

    public void add(Node child, int columnIndex, int rowIndex, int colSpan, int rowSpan, Priority... expansionPriorities)
    {
        super.add(child, columnIndex, rowIndex, colSpan, rowSpan);
        applyPriorities(child, expansionPriorities);
    }

    @Deprecated
    public StyledButton addImageButton(ImageView imageHolder, int columnIndex, int rowIndex, Priority... expansionPriorities)
    {
        return addImageButton(imageHolder, columnIndex, rowIndex, 1, 1, expansionPriorities);
    }

    @Deprecated
    public StyledButton addImageButton(ImageView imageHolder, int columnIndex, int rowIndex, int colSpan, int rowSpan, Priority... expansionPriorities)
    {
        StyledButton button = new StyledButton();

        button.setPrefWidth(imageHolder.getFitWidth());
        button.setPrefHeight(imageHolder.getFitWidth());

        add(imageHolder, columnIndex, rowIndex, colSpan, rowSpan, expansionPriorities);
        add(button, columnIndex, rowIndex, colSpan, rowSpan, expansionPriorities);

        return button;
    }
}
