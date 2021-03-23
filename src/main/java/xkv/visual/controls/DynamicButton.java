package xkv.visual.controls;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import xkv.visual.VisualResourceLoader;
import xkv.visual.panels.StandardGridPane;

import java.util.function.Supplier;

public class DynamicButton extends StandardGridPane
{
    protected Image image, hoveredImage;

    protected final ImageView view = new ImageView(), hoveredView = new ImageView();

    private final FadeTransition fade;

    protected Supplier<Boolean> onClicked = () -> true;

    protected Supplier<Boolean> qualifier = () -> true;
    protected Runnable action = () -> {};

    public static final double FADE_DURATION = 300;

    protected DynamicButton()
    {
        fade = new FadeTransition();
        fade.setDuration(Duration.millis(FADE_DURATION));
        fade.setToValue(1.0D);
        fade.setFromValue(0.0D);
        fade.setNode(hoveredView);

        hoveredView.setOpacity(0.0D);

        hoverProperty().addListener((v, wsHov, isHov) ->
        {
            fade.setRate(isHov ? 1.0D : -1.0D);
            fade.play();
        });

        setPickOnBounds(true);

        super.add(view, 0, 0, Priority.ALWAYS, Priority.ALWAYS);
        super.add(hoveredView, 0, 0, Priority.ALWAYS, Priority.ALWAYS);

        setOnMouseClicked(value ->
        {
            if (qualifier.get())
            {
                action.run();
            }
        });
    }

    public static DynamicButton configure(Image image, Image hovered)
    {
        DynamicButton button = new DynamicButton();

        button.image = image;
        button.hoveredImage = hovered;

        button.view.setImage(image);
        button.hoveredView.setImage(hovered);

        return button;
    }

    public void bindQualifier(Supplier<Boolean> qualifier)
    {
        this.qualifier = qualifier;
    }

    public void bindAction(Runnable action)
    {
        this.action = action;
    }

    public void resize(double dimension)
    {
        VisualResourceLoader.scaleImageView(view, dimension);
        VisualResourceLoader.scaleImageView(hoveredView, dimension);
    }

    public DynamicButton configureTooltip(String tooltip)
    {
        Tooltip.install(this, new Tooltip(tooltip));

        return this;
    }

    @Deprecated
    @Override
    public void add(Node child, int columnIndex, int rowIndex, Priority... expansionPriorities)
    {

    }

    @Deprecated
    @Override
    public void add(Node child, int columnIndex, int rowIndex, int colSpan, int rowSpan, Priority... expansionPriorities)
    {

    }

    @Deprecated
    @Override
    public void add(Node child, int columnIndex, int rowIndex)
    {

    }

    @Deprecated
    @Override
    public void add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
    {

    }
}
