package xkv.visual.images;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class ImageUtil
{
    public static boolean areImagesEqual(Image image1, Image image2)
    {
        if (image1 == null)
        {
            return image2 == null;
        }

        if (image2 == null)
        {
            return false;
        }

        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
        {
            return false;
        }

        PixelReader reader1 = image1.getPixelReader();
        PixelReader reader2 = image2.getPixelReader();

        for (int col = 0; col < image1.getWidth(); col ++)
        {
            for (int row = 0; row < image1.getHeight(); row ++)
            {
                if (reader1.getArgb(col, row) != reader2.getArgb(col, row))
                {
                    return false;
                }
            }
        }

        return true;
    }
}
