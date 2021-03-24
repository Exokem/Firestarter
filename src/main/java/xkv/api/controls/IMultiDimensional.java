package xkv.api.controls;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

@SuppressWarnings("unchecked")
public interface IMultiDimensional<R extends Region>
{
    default R resize(double value, Dimension... dimensions) throws ClassCastException
    {
        if (this instanceof Region)
        {
            for (Dimension dimension : dimensions)
            {
                dimension.apply((Region) this, value);
            }

            return (R) this;
        }

        return null;
    }

    default R expand(Priority horizontal, Priority vertical) throws ClassCastException
    {
        if (this instanceof Region)
        {
            Region region = (Region) this;

            if (horizontal != null)
            {
                GridPane.setHgrow(region, horizontal);
            }

            if (vertical != null)
            {
                GridPane.setVgrow(region, vertical);
            }

            return (R) region;
        }

        return null;
    }

    default R alignV(VPos alignment) throws ClassCastException
    {
        if (this instanceof Region)
        {
            Region region = (Region) this;

            GridPane.setValignment(region, alignment);

            return (R) region;
        }

        return null;
    }

    default R alignH(HPos alignment) throws ClassCastException
    {
        if (this instanceof Region)
        {
            Region region = (Region) this;

            GridPane.setHalignment(region, alignment);

            return (R) region;
        }

        return null;
    }
}
