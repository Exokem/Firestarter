package xkv.visual.css;

public enum Style
{
    DEBUG("debug"), SHADOW("shadow"), SMALL_SHADOW("small_shadow"), INSET("inset-object"), BSP_BUTTON("bsp-button");

    private final String style;

    Style(String style)
    {
        this.style = style;
    }

    public String style()
    {
        return this.style;
    }

    @Override
    public String toString()
    {
        return style;
    }
}
