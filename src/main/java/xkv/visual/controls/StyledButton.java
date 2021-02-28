package xkv.visual.controls;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;
import xkv.visual.css.FadeButtonSkin;
import xkv.visual.css.IStylable;

public class StyledButton extends Button implements IStylable
{
    public StyledButton(String text)
    {
        super(text);
    }

    public StyledButton()
    {

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
