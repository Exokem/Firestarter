package xkv.visual.css;

public enum PanelStyle
{
    INSET("inset-object");

    private final String style;

    PanelStyle(String style)
    {
        this.style = style;
    }

    public String style()
    {
        return this.style;
    }
}
