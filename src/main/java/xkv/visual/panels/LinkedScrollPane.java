package xkv.visual.panels;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import xkv.visual.css.Style;
import xkv.visual.panels.audion.AudionPanel;

import java.util.HashMap;
import java.util.Map;

public class LinkedScrollPane<ITEM extends Node, LINK> extends StandardScrollPane<ITEM>
{
    private final Map<ITEM, LINK> linker = new HashMap<>();

    private final Map<LINK, ITEM> dualLinker = new HashMap<>();

    public LinkedScrollPane()
    {
        super();
    }

    public void link(ITEM item, LINK link)
    {
        super.addItem(item);

        if (item instanceof Region)
        {
            Region region = (Region) item;
            region.setMaxWidth(Double.MAX_VALUE);
            region.setMinHeight(AudionPanel.PANEL_HEIGHT / 20);

            Style.apply(region, Style.SCROLLPANE_BUTTON, Style.SQUARE);
        }

        linker.put(item, link);
        dualLinker.put(link, item);
    }

    public LINK getLink(ITEM item)
    {
        return linker.get(item);
    }

    public ITEM getItem(LINK link)
    {
        return dualLinker.get(link);
    }

    public void remove(ITEM item)
    {
        LINK link = linker.get(item);

        linker.remove(item);
        dualLinker.remove(link);

        super.remove(item);
    }

    public void remove(LINK link)
    {
        ITEM item = dualLinker.get(link);

        dualLinker.remove(link);
        linker.remove(item);

        super.remove(item);
    }

    public LinkedScrollPane<ITEM, LINK> width(double width)
    {
        super.width(width);

        return this;
    }
}
