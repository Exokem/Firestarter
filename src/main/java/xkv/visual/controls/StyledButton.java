package xkv.visual.controls;

import javafx.scene.control.Button;
import xkv.visual.css.FadeButtonSkin;
import xkv.visual.css.IStylable;

public class StyledButton extends Button implements IStylable
{
    public StyledButton(String text)
    {
        super(text);
        setSnapToPixel(true);
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

    public StyledButton animate()
    {
        this.setSkin(FadeButtonSkin.fadeCycle(this));

        return this;
    }
}
