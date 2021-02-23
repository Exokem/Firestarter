package xkv.visual.css;

import javafx.scene.Parent;
import javafx.scene.layout.Region;

public class PanelApplicators
{
    public static void applyInset(Parent object)
    {
        object.setStyle(".inset-object");
    }

    public static <C extends Region> C inset(C object)
    {
        object.setStyle(".inset-object");

        return object;
    }
}
