package xkv.visual.panels.audion;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import xkv.Firestarter;
import xkv.content.Album;
import xkv.content.Track;
import xkv.data.VisualResourceLoader;
import xkv.visual.HiddenRectangle;
import xkv.visual.controls.DynamicButton;
import xkv.visual.controls.DynamicToggledButton;
import xkv.visual.controls.HoverLabel;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.PaneFactory;
import xkv.visual.panels.StandardGridPane;

import javax.sound.sampled.*;
import java.io.IOException;

import static xkv.data.ResourceLoader.*;
import static xkv.data.VisualResourceLoader.DEFAULT_IMAGE;

public class AudionPlayer
{
    private static boolean active = false;
    private static Track activeTrack = null;
    private static Album activeAlbum = null;

    private static boolean randomize = false, repeat = false;

    public static boolean activate(Track track)
    {
        assert track != null;

        activeTrack = track;
//        activeAlbum = Audion.Data.activeAlbum;

        try
        {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(track.data());

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();



        }

        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public static boolean activable()
    {
        return !active;
    }

    public static boolean deactivable()
    {
        return active;
    }

    protected static class View
    {
        protected static void initialize() {}

        private static final HoverLabel TITLE = HoverLabel.configure("Track 1", "gray", "white", Style.SMALL_TITLE_BOLD);
        private static final HoverLabel AUTHOR = HoverLabel.configure("Author", "gray", "white", Style.SUBTITLE);

        private static final ProgressBar PROGRESS = progressView();

        protected static final StandardGridPane OVERLAY = overlay();
        private static final ImageView ICON_VIEW = new ImageView(DEFAULT_IMAGE);
        protected static final BorderPane ICON = icon();

        protected static double SCALE = Audion.Scale.MINOR.factor;

        private static final FadeTransition OVERLAY_FADE = overlayFade();

        private static ProgressBar progressView()
        {
            ProgressBar progress = new ProgressBar();

            return progress;
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

            iconContainer.setMaxWidth(SCALE * Audion.panelWidth());

            VisualResourceLoader.scaleImageView(ICON_VIEW, SCALE * Firestarter.width());
            DynamicResizeable.addResizeListener(() -> VisualResourceLoader.scaleImageView(ICON_VIEW, SCALE * Firestarter.width()));

            return iconContainer;
        }

        private static StandardGridPane overlay()
        {
            StandardGridPane overlay = PaneFactory.autoPaddedGrid(0, 0, 1, 4);

            GridPane labels = new GridPane();
            StandardGridPane status = PaneFactory.autoPaddedGrid(0, 5, 2);

            labels.add(TITLE, 0, 0);
            labels.add(AUTHOR, 0, 1);

            DynamicToggledButton playPause = DynamicToggledButton.configure(PLA_ICN, PLA_HOV, PAU_ICN, PAU_HOV);
            playPause.bindQualifier(() ->
            {
                if (playPause.selected()) return activable();
                return deactivable();
            });

            DynamicButton previous = DynamicButton.configure(PRE_ICN, PRE_HOV).configureTooltip("Previous");

            DynamicButton next = DynamicButton.configure(NXT_ICN, NXT_HOV).configureTooltip("Next");

            DynamicToggledButton shuffle = (DynamicToggledButton) DynamicToggledButton.configure(RND_ICN, RND_HOV, RND_ONI, RND_ONH).configureTooltip("Randomize");
            shuffle.bindAction(() -> randomize = shuffle.selected());

            DynamicToggledButton repeat = (DynamicToggledButton) DynamicToggledButton.configure(RPT_ICN, RPT_HOV, RPT_ONI, RPT_ONH).configureTooltip("Repeat");
            repeat.bindAction(() -> AudionPlayer.repeat = repeat.selected());

            // TODO: button functionality

            status.add(shuffle, 1, 1);
            status.add(previous, 2, 1);
            status.add(playPause, 3, 1);
            status.add(next, 4, 1);
            status.add(repeat, 5, 1);

            status.setAlignment(Pos.CENTER);

            Rectangle upset = new Rectangle(0, 0);
            upset.setVisible(false);

            DynamicResizeable.addResizeListener(() ->
            {
                double windowWidth = Firestarter.width();

                PROGRESS.setMaxWidth(SCALE * windowWidth);
                PROGRESS.setMaxHeight(0.015D * SCALE * windowWidth);
                TITLE.resizeText(0.06D * SCALE * windowWidth);
                AUTHOR.resizeText(0.04D * SCALE * windowWidth);

                double actionSize = 0.18 * SCALE * windowWidth;

                shuffle.resize(actionSize);
                previous.resize(actionSize);
                playPause.resize(actionSize);
                next.resize(actionSize);
                repeat.resize(actionSize);
            });

            final double inset = 14, insetAlt = 8;

            overlay.add(new HiddenRectangle(inset, insetAlt), 0, 0);
            overlay.add(labels, 1, 1, Priority.ALWAYS);
            overlay.add(new BorderPane(), 1, 2, Priority.ALWAYS, Priority.ALWAYS);
            overlay.add(status, 1, 3, Priority.ALWAYS);
            overlay.add(PROGRESS, 1, 4, Priority.ALWAYS);
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
}
