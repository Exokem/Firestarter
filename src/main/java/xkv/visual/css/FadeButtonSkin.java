package xkv.visual.css;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.util.Duration;

public class FadeButtonSkin extends ButtonSkin
{
    public FadeButtonSkin(Button control)
    {
        super(control);
    }

    public static FadeButtonSkin fadeCycle(Button control)
    {
        return fadeCycle(control, 400, 1, 0);
    }

    public static FadeButtonSkin fadeCycle(Button control, double duration, double toValue, double fromValue)
    {
        FadeButtonSkin fade = new FadeButtonSkin(control);

        FadeTransition in = new FadeTransition(Duration.millis(duration), control);
        in.setNode(control);
        in.setFromValue(fromValue);
        in.setToValue(toValue);

        control.setOpacity(fromValue);

        control.hoverProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue)
            {
                in.setRate(1.0);
            }
            else
            {
                in.setRate(-1.0);
            }

            in.play();
        }));

        return fade;
    }
}
