package xkv.visual.panels;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;

import java.util.*;

public class StandardScrollPanel<I extends Node> extends ScrollPane implements IStylable
{
    private static final int DEFAULT_SLATES = 10;

    private final List<I> items = new ArrayList<>();

    private final GridPane itemGrid;

    protected StandardScrollPanel()
    {
        this(DEFAULT_SLATES);
    }

    protected StandardScrollPanel(int slates)
    {
        this.itemGrid = PanelFactory.paddedGrid(0, 3, slates + 2, Style.INSET);

        this.addVisualStyle(Style.INSET);

        // Allow contents to use entire space
        this.setFitToWidth(true);
        this.setFitToHeight(true);

        this.setContent(itemGrid);
    }

    public void addItem(I item)
    {
        if (item instanceof IStylable)
        {
            ((IStylable) item).addVisualStyles(this.itemStyles);
        } else
        {
            this.itemStyles.forEach(panelStyle -> item.getStyleClass().add(panelStyle.style()));
        }

        // Allow buttons to use entire width
        GridPane.setHgrow(item, Priority.ALWAYS);
        item.maxWidth(Double.MAX_VALUE);

        int indx = this.items.size();
        this.items.add(item);

        this.itemGrid.add(item, 1, indx);
    }

    public StandardScrollPanel<I> width(double width)
    {
        this.setPrefWidth(width);

        return this;
    }

    private final List<Style> itemStyles = new ArrayList<>();

    @Override
    public boolean container()
    {
        return true;
    }

    @Override
    public List<Style> itemStyles()
    {
        return itemStyles;
    }

    @Override
    public void requestFocus()
    {
    }
}
