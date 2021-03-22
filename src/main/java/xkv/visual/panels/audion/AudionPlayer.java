package xkv.visual.panels.audion;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import xkv.visual.VisualResourceLoader;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import static xkv.visual.VisualResourceLoader.DEFAULT_IMAGE;

public class AudionPlayer
{
    private static final Label TITLE = new Label("Track 1");
    private static final Label AUTHOR = new Label("Author");

    private static final ImageView ICON_VIEW = new ImageView(DEFAULT_IMAGE);

    protected static final BorderPane ICON = icon();
    protected static final StandardGridPane OVERLAY = overlay();
//    protected static final StandardGridPane LABEL = label();

    protected static double SCALE = AudionAlbumSelect.SCALE;

    private static BorderPane icon()
    {
        BorderPane iconContainer = PaneFactory.styledBorderPane(ICON_VIEW, Style.INSET);

        VisualResourceLoader.scaleImageView(ICON_VIEW, AudionAlbumSelect.ALBUM_PROVIDER.getWidth());
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(ICON_VIEW, AudionAlbumSelect.ALBUM_PROVIDER.getWidth()));

        return iconContainer;
    }

    private static StandardGridPane overlay()
    {
        StandardGridPane overlay = PaneFactory.autoPaddedGrid(10, 1, 3);

        GridPane labels = new GridPane();

//        Style.apply(labels, Style.DEBUG);

//        labels.setVgap(3);
        labels.add(TITLE, 0, 0);
        labels.add(AUTHOR, 0, 1);

        Style.apply(TITLE, Style.SMALL_TITLE_BOLD, Style.OVERLAY_TEXT);
        Style.apply(AUTHOR, Style.SUBTITLE, Style.OVERLAY_TEXT);

        overlay.add(labels, 1, 1, Priority.ALWAYS);

        return overlay;
    }

//    private static StandardGridPane label()
//    {
//        StandardGridPane labelPane = PaneFactory.autoPaddedGrid(10, 1, 2, Style.INSET);
//
//        labelPane.add(TITLE, 1, 1);
//        labelPane.add(AUTHOR, 1, 2);
//
//        Style.apply(TITLE, Style.SMALL_TITLE_BOLD);
//        Style.apply(AUTHOR, Style.SUBTITLE);
//
//        return labelPane;
//    }
}
