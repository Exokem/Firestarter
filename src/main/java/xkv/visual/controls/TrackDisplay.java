package xkv.visual.controls;

import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import xkv.api.controls.Dimension;
import xkv.content.Track;
import xkv.visual.css.Style;
import xkv.visual.panels.DynamicResizeable;
import xkv.visual.panels.StandardGridPane;
import xkv.visual.panels.audion.Audion;

import static xkv.ResourceLoader.TRK_HOV;
import static xkv.ResourceLoader.TRK_ICN;

public class TrackDisplay
{
    public StandardGridPane getAndUpdate()
    {
        title.setText(track.identifier());
        author.setText(track.author());
//        duration.setText();

        return container;
    }

    private final Track track;
    private final StandardGridPane container;

    private final AdjustableLabel title = new AdjustableLabel("", Style.SUBTITLE)
            .alignment(Pos.CENTER_LEFT)
            .expand(null, Priority.ALWAYS);
    private final AdjustableLabel author = new AdjustableLabel("", Style.SUBTITLE)
            .alignment(Pos.CENTER_LEFT)
            .expand(null, Priority.ALWAYS);
    private final AdjustableLabel duration = new AdjustableLabel("0:00", Style.SUBTITLE)
            .alignment(Pos.CENTER_RIGHT)
            .expand(null, Priority.ALWAYS)
            .pad(0D, 10D, 0D, 0D);

    public TrackDisplay(Track track)
    {
        this.track = track;
        this.container = container();
    }

    private StandardGridPane container()
    {
        StandardGridPane container = new StandardGridPane(Style.SCROLLPANE_BUTTON);

        double height = Audion.panelHeight() / 20;

        final DynamicButton play = DynamicButton.configure(TRK_ICN, TRK_HOV).configureTooltip("Play this track").resize(height);

        DynamicResizeable.addResizeListener(() ->
        {
            title.resize(Audion.scaleWidth(Audion.Scale.MAJOR.adjust(0.5D)), Dimension.MAX_WIDTH, Dimension.MIN_WIDTH);
            author.resize(Audion.scaleWidth(Audion.Scale.MAJOR.adjust(12.0D / 30.0D)), Dimension.MAX_WIDTH, Dimension.PREF_WIDTH);
            duration.resize(Audion.scaleWidth(Audion.Scale.MAJOR.adjust(2.0D / 30.0D)), Dimension.MAX_WIDTH, Dimension.MIN_WIDTH);
        });

        container.add(play, 0, 0, Priority.NEVER);
        container.add(title, 1, 0, Priority.SOMETIMES);
        container.add(author, 2, 0, Priority.SOMETIMES);
        container.add(duration, 3, 0, Priority.ALWAYS);

        container.setMaxHeight(height);

        return container;
    }


}
