package xkv.visual.controls;

import javafx.scene.control.Button;
import xkv.visual.css.FadeButtonSkin;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;

public class StyledButton extends Button implements IStylable
{
    public StyledButton(String text)
    {
        super(text);
        setSnapToPixel(true);
        addVisualStyle(Style.UI_BUTTON);
    }

    public StyledButton(String text, Style... styles)
    {
        this(text);
        addVisualStyle(styles);
    }

    public StyledButton()
    {
        setSnapToPixel(true);
    }

    public StyledButton square(double width)
    {
        setPrefWidth(width);
        setPrefHeight(width);

        return this;
    }

    public StyledButton width(double width)
    {
        setPrefWidth(width);

        return this;
    }

    public StyledButton animate()
    {
        this.setSkin(FadeButtonSkin.fadeCycle(this));

        return this;
    }

    public StyledButton action(Runnable action)
    {
        this.setOnAction(value -> action.run());

        return this;
    }
}
