package xkv.visual.controls;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class DynamicToggledButton extends DynamicButton
{
    private Image unselectedImage, unselectedHoveredImage;
    private Image selectedImage, selectedHoveredImage;

    private boolean selected = false;

    private DynamicToggledButton()
    {
        super();

        setOnMouseClicked(this::onClicked);
    }

    public void onClicked(MouseEvent value)
    {
        if (this.onClicked.get())
        {
            selected = !selected;
            view.setImage(selected ? selectedImage : unselectedImage);
            hoveredView.setImage(selected ? selectedHoveredImage : unselectedHoveredImage);
        }
    }

    public static DynamicToggledButton configure(Image unselected, Image unselectedHovered, Image selected, Image selectedHovered)
    {
        DynamicToggledButton button = new DynamicToggledButton();

        button.unselectedImage = unselected; button.unselectedHoveredImage = unselectedHovered;
        button.selectedImage = selected; button.selectedHoveredImage = selectedHovered;

        button.view.setImage(unselected);
        button.hoveredView.setImage(unselectedHovered);

        return button;
    }
}
