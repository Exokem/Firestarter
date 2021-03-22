package xkv.visual.panels.audion;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import xkv.Firestarter;
import xkv.visual.HiddenRectangle;
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

//    private static final StandardGridPane PROGRESS = progress();
    private static final ProgressBar PROGRESS = progressView();

    protected static final StandardGridPane OVERLAY = overlay();
    private static final ImageView ICON_VIEW = new ImageView(DEFAULT_IMAGE);
    protected static final BorderPane ICON = icon();

    protected static double SCALE = AudionAlbumSelect.SCALE;

    private static final FadeTransition OVERLAY_FADE = overlayFade();

    private static ProgressBar progressView()
    {
        ProgressBar progress = new ProgressBar();


//        progress.setProgress(0.5D);

        return progress;
    }

//    private static StandardGridPane progress()
//    {
//        StandardGridPane progress = new StandardGridPane();
//
//        Rectangle backing = new Rectangle(10, 0);
//
//        DynamicResizeable.addResizeListener(() -> backing.setWidth(progress.getWidth()));
//
//        Style.apply(progress, Style.DEBUG);
//
//        return progress;
//    }

    private static double progressValue()
    {
        return 0.0D;
    }

    private static FadeTransition overlayFade()
    {
        FadeTransition overlayFade = new FadeTransition();
        overlayFade.setNode(OVERLAY);
        overlayFade.setToValue(1.0D);
        overlayFade.setFromValue(0.0D);
        overlayFade.setDuration(Duration.millis(400));
        return overlayFade;
    }

    private static BorderPane icon()
    {
        BorderPane iconContainer = PaneFactory.styledBorderPane(ICON_VIEW, Style.INSET);

        iconContainer.setMaxWidth(SCALE * AudionPanel.PANEL_WIDTH);

        VisualResourceLoader.scaleImageView(ICON_VIEW, AudionAlbumSelect.ALBUM_PROVIDER.getWidth());
        DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(ICON_VIEW, SCALE * Firestarter.firestarter.getWidth()));

        return iconContainer;
    }

    private static StandardGridPane overlay()
    {
        StandardGridPane overlay = PaneFactory.autoPaddedGrid(0, 0, 1, 4, Style.RELATIVE);

        GridPane labels = new GridPane();
        StandardGridPane status = PaneFactory.autoPaddedGrid(0, 1, 1);

        labels.add(TITLE, 0, 0);
        labels.add(AUTHOR, 0, 1);

        Style.apply(TITLE, Style.SMALL_TITLE_BOLD, Style.OVERLAY_TEXT);
        Style.apply(AUTHOR, Style.SUBTITLE, Style.OVERLAY_TEXT);

        status.add(PROGRESS, 1, 1, Priority.ALWAYS);

        Rectangle upset = new Rectangle(0, 0);
        upset.setVisible(false);

        DynamicResizeable.addResizeListener(() ->
        {
            double windowWidth = Firestarter.firestarter.getWidth();

            PROGRESS.setMaxWidth(SCALE * windowWidth);
            PROGRESS.setMaxHeight(0.015D * SCALE * windowWidth);
            TITLE.setStyle(String.format("-fx-font-size: %f", 0.06D * SCALE * windowWidth));
            AUTHOR.setStyle(String.format("-fx-font-size: %f", 0.04D * SCALE * windowWidth));
        });

        final double inset = 14, insetAlt = 8;

        overlay.add(new HiddenRectangle(inset, insetAlt), 0, 0);
        overlay.add(labels, 1, 1, Priority.ALWAYS);
        overlay.add(new BorderPane(), 1, 2, Priority.ALWAYS, Priority.ALWAYS);
        overlay.add(status, 1, 3, Priority.ALWAYS);
        overlay.add(new HiddenRectangle(inset, inset), 0, 5);
        overlay.add(new HiddenRectangle(inset, inset), 2, 5);
        overlay.setOpacity(0.0D);

        overlay.hoverProperty().addListener((observable, wasHovered, isHovered) ->
        {
            OVERLAY_FADE.setRate(isHovered ? 1.0D : -1.0D);
            OVERLAY_FADE.play();
        });

        return overlay;
    }
}
