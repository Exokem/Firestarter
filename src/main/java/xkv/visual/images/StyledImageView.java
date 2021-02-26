package xkv.visual.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import xkv.visual.css.IStylable;
import xkv.visual.css.Style;


import java.util.Collection;

public class StyledImageView extends ImageView implements IStylable
{
    public StyledImageView(Image image) {
        super(image);
    }
}
