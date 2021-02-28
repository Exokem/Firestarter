package xkv.visual.panels.audion;

import javafx.scene.image.ImageView;
import xkv.content.Album;


public class AudionAlbumView
{
    protected static ImageView openAlbumImageView = null;

    protected static void openAlbum(Album album)
    {
        if (openAlbumImageView != null && album.image() != null)
        {
            openAlbumImageView.setImage(album.image());
        }
    }
}
