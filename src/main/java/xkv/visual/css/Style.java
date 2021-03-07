package xkv.visual.css;

import javafx.scene.layout.Region;

public enum Style
{
    DEBUG("debug"), SHADOW("shadow"), SMALL_SHADOW("small-shadow"), INSET("inset-object"), BSP_BUTTON("bsp-button"),
    TITLE_BOLD("title-bold"), SUBTITLE("subtitle"), WHITE("white"), SCROLLPANE_BUTTON("scrollpane-button"),
    SQUARE("square"), SPBC("scrollpane-button-child"), UI_BUTTON("ui-button");

    private final String style;

    Style(String style)
    {
        this.style = style;
    }

    public String style()
    {
        return this.style;
    }

    public static void apply(Region region, Style... styles)
    {
        for (Style style : styles)
        {
            region.getStyleClass().add(style.style());
        }
    }

    @Override
    public String toString()
    {
        return style;
    }
}
