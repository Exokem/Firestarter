package xkv.visual.panels;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum BorderSection
{
    TOP, RIGHT, BOTTOM, LEFT, CENTER;

    public static Set<BorderSection> horizontals()
    {
        return Set.of(TOP, BOTTOM);
    }

    public static Set<BorderSection> verticals()
    {
        return Set.of(LEFT, RIGHT);
    }

    public static Set<BorderSection> valueSet()
    {
        return Set.of(TOP, RIGHT, BOTTOM, LEFT, CENTER);
    }

    public static Set<BorderSection> edgeSet()
    {
        return Set.of(TOP, RIGHT, BOTTOM, LEFT);
    }

    public void place(BorderPane pane, Node content)
    {
        if (this == (TOP))
        {
            pane.setTop(content);
        }
        else if (this == (RIGHT))
        {
            pane.setRight(content);
        }
        else if (this == BOTTOM)
        {
            pane.setBottom(content);
        }
        else if (this == LEFT)
        {
            pane.setLeft(content);
        }
        else
        {
            pane.setCenter(content);
        }
    }
}
