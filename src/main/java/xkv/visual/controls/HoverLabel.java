package xkv.visual.controls;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import xkv.visual.css.Style;
import xkv.visual.panels.StandardGridPane;

public class HoverLabel extends StandardGridPane
{
    private Label label, hoveredLabel;

    private final FadeTransition fade;

    public static final double FADE_DURATION = 300;

    private HoverLabel(String text, Style... styles)
    {
        label = new Label(text);
        hoveredLabel = new Label(text);

        fade = new FadeTransition();
        fade.setDuration(Duration.millis(FADE_DURATION));
        fade.setToValue(1.0D);
        fade.setFromValue(0.0D);
        fade.setNode(hoveredLabel);

        Style.apply(label, styles);
        Style.apply(hoveredLabel, styles);

        hoveredLabel.setOpacity(0.0D);

        hoverProperty().addListener((v, wsHov, isHov) ->
        {
            fade.setRate(isHov ? 1.0D : -1.0D);
            fade.play();
        });

        setPickOnBounds(true);


        add(label, 0, 0);
        add(hoveredLabel, 0, 0);
    }

    public static HoverLabel configure(String text, String color, String hoveredColor, Style... styles)
    {
        HoverLabel label = new HoverLabel(text, styles);

        label.label.setTextFill(Paint.valueOf(color));
        label.hoveredLabel.setTextFill(Paint.valueOf(hoveredColor));

        return label;
    }

    public void resizeText(double size)
    {
        label.setStyle(String.format("-fx-font-size: %f", size));
        hoveredLabel.setStyle(String.format("-fx-font-size: %f", size));
    }

    public void setText(String text)
    {
        label.setText(text);
        hoveredLabel.setText(text);
    }

    public void setTextFill(Paint paint)
    {
        label.setTextFill(paint);
    }

    public void setHoveredTextFill(Paint paint)
    {
        hoveredLabel.setTextFill(paint);
    }
}
