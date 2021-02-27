package xkv.visual.css;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.util.Duration;

public class FadeButtonSkin extends ButtonSkin
{
    /**
     * Creates a new ButtonSkin instance, installing the necessary child
     * nodes into the Control {@link Control#getChildren() children} list, as
     * well as the necessary input mappings for handling key, mouse, etc events.
     *
     * @param control The control that this skin should be installed onto.
     */
    public FadeButtonSkin(Button control)
    {
        super(control);

        FadeTransition in = new FadeTransition(Duration.millis(400), control);
        in.setNode(control);
        in.setFromValue(0);
        in.setToValue(1);

        control.setOpacity(0);

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
    }
}
