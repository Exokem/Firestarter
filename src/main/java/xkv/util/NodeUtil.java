package xkv.util;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public class NodeUtil
{
    public static void tooltip(Node node, String tooltip)
    {
        Tooltip.install(node, new Tooltip(tooltip));
    }
}
