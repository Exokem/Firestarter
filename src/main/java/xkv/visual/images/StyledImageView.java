package xkv.visual.images;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import xkv.visual.VisualResourceLoader;
import xkv.visual.css.IStylable;

public class StyledImageView extends ImageView implements IStylable
{
    public StyledImageView(Image image) {
        super(image);
    }

    public StyledImageView resizeByDefault(double size)
    {
        VisualResourceLoader.scaleImageView(this, size);
        return this;
    }

    public void configureTooltip(String tooltip)
    {
        Tooltip.install(this, new Tooltip(tooltip));
    }

    public void configureHover(final Image normal, final Image hovered)
    {
        this.setPickOnBounds(true);

        this.hoverProperty().addListener((observable, wasHovered, isHovered) ->
        {
            if (isHovered)
            {
                this.setImage(hovered);
            }
            else
            {
                this.setImage(normal);
            }
        });
    }
}
