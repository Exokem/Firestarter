package xkv.visual.controls;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;
import xkv.visual.panels.StandardGridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MultiButton extends StandardGridPane implements IStylable
{
    protected final List<Button> buttons = new ArrayList<>();

    protected MultiButton()
    {

    }

    protected MultiButton(double gap, int columns, int rows)
    {
        setGap(gap);
        pad(columns, rows);
    }

    private final List<Style> buttonStyles = new ArrayList<>();

    @Override
    public boolean container()
    {
        return true;
    }

    @Override
    public List<Style> itemStyles()
    {
        return buttonStyles;
    }

    public void add(Node child, int columnIndex, int rowIndex, Priority expandH, Priority expandV)
    {
        add(child, columnIndex, rowIndex, 1, 1, expandH, expandV);
    }

    @Override
    public void add(Node child, int columnIndex, int rowIndex)
    {
        add(child, columnIndex, rowIndex, 1, 1);
    }

    public void add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan, Priority expandH, Priority expandV)
    {
        if (child instanceof Button)
        {
            Button button = (Button) child;

            if (expandH != null)
            {
                button.setMaxWidth(Double.MAX_VALUE);
                GridPane.setHgrow(button, expandH);
            }

            if (expandV != null)
            {
                button.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(button, expandV);
            }

            if (child instanceof IStylable)
            {
                ((IStylable) child).addVisualStyles(this.buttonStyles);
            } else
            {
                this.buttonStyles.forEach(buttonStyle -> child.getStyleClass().add(buttonStyle.style()));
            }

            buttons.add(button);
            super.add(child, columnIndex, rowIndex, colspan, rowspan);
        }
    }

    @Override
    public void add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
    {
        add(child, columnIndex, rowIndex, colspan, rowspan, Priority.SOMETIMES, Priority.SOMETIMES);
    }
}
