package xkv.visual.panels;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardScrollPane<I extends Node> extends ScrollPane implements IStylable
{
    private static final int DEFAULT_SLATES = 10;

    private final List<I> items = new ArrayList<>();

    private final Map<I, Integer> indexMap = new HashMap<>();

    private final GridPane itemGrid;

    protected StandardScrollPane()
    {
        this(DEFAULT_SLATES);
    }

    protected StandardScrollPane(int slates)
    {
        itemGrid = PaneFactory.autoPaddedGrid(0, 1, slates, Style.WHITE);

        itemGrid.setSnapToPixel(true);

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
        this.indexMap.put(item, indx);
    }

    public void remove(I item)
    {
        this.items.remove(item);

        this.itemGrid.getChildren().remove(item);

        for (int listIndx = 0; listIndx < this.items.size(); listIndx++)
        {
            I oldItem = items.get(listIndx);

            int mapIndx = indexMap.get(oldItem);

            if (mapIndx != listIndx)
            {
                itemGrid.getChildren().remove(oldItem);
                itemGrid.add(oldItem, 1, listIndx);
            }
        }
    }

    public I top()
    {
        return items.get(0);
    }

    public StandardScrollPane<I> width(double width)
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
    public void requestFocus()
    {
    }
}
