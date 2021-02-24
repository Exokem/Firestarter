package xkv.visual.css;

import javafx.scene.layout.Region;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface IStylable
{
    default boolean container()
    {
        return false;
    }

    default List<Style> itemStyles()
    {
        return null;
    }

    default void addVisualClass(String visualClass)
    {
        if (this instanceof Region)
        {
            Region region = (Region) this;

            region.getStyleClass().add(visualClass);
        }
    }

    default void addVisualStyle(Style... style)
    {
        addVisualStyles(Arrays.asList(style));
    }

    default void addVisualStyles(Collection<Style> styles)
    {
        if (container())
        {
            List<Style> itemStyles = this.itemStyles();
            if (itemStyles != null)
            {
                itemStyles.addAll(styles);
            }
        } else
        {
            styles.forEach(style -> addVisualClass(style.style()));
        }
    }
}
