package xkv;

import javafx.scene.image.Image;
import xkv.visual.VisualResourceLoader;

public class ResourceLoader
{
    protected static void loadResources()
    {

    }

    public static final Image DEL_ICN = VisualResourceLoader.loadImage(VisualResourceLoader.ResourceHeader.UI_BUTTONS, "x_button.png");
    public static final Image DEL_HOV = VisualResourceLoader.loadImage(VisualResourceLoader.ResourceHeader.UI_BUTTONS, "x_button_hover.png");

    public static final Image IMG_ICN = VisualResourceLoader.loadImage(VisualResourceLoader.ResourceHeader.UI_BUTTONS, "image_button.png");
    public static final Image IMG_HOV = VisualResourceLoader.loadImage(VisualResourceLoader.ResourceHeader.UI_BUTTONS, "image_button_hover.png");

    public static final Image RNM_ICN = VisualResourceLoader.loadImage(VisualResourceLoader.ResourceHeader.UI_BUTTONS, "rename.png");
    public static final Image RNM_HOV = VisualResourceLoader.loadImage(VisualResourceLoader.ResourceHeader.UI_BUTTONS, "rename_hover.png");
}
