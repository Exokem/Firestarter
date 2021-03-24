package xkv.visual.controls;

import javafx.scene.image.Image;

public class DynamicToggledButton extends DynamicButton
{
    public static DynamicToggledButton configure(Image unselected, Image unselectedHovered, Image selected, Image selectedHovered)
    {
        DynamicToggledButton button = new DynamicToggledButton();

        button.unselectedImage = unselected; button.unselectedHoveredImage = unselectedHovered;
        button.selectedImage = selected; button.selectedHoveredImage = selectedHovered;

        button.view.setImage(unselected);
        button.hoveredView.setImage(unselectedHovered);

        return button;
    }

    public boolean selected()
    {
        return selected;
    }

    public void switchSelect()
    {
        selected = !selected;
        view.setImage(selected ? selectedImage : unselectedImage);
        hoveredView.setImage(selected ? selectedHoveredImage : unselectedHoveredImage);
    }

    private Image unselectedImage, unselectedHoveredImage;
    private Image selectedImage, selectedHoveredImage;
    private boolean selected = false;

    private DynamicToggledButton()
    {
        super();

        setOnMouseClicked(value ->
        {
            if (qualifier.get())
            {
                action.run();
                switchSelect();
            }
        });
    }
}
