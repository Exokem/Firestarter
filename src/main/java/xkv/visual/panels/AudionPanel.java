package xkv.visual.panels;

import javafx.application.Platform;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import xkv.visual.css.PanelApplicators;
import xkv.visual.css.PanelStyle;

public class AudionPanel extends BorderPane
{
    private final GridPane layoutContent = PanelApplicators.inset(PanelFactory.paddedGrid(10, 3, 3, PanelStyle.INSET));

    private AudionPanel()
    {
        Rectangle testContent = new Rectangle(1024, 512);
        layoutContent.add(testContent, 1, 1);
        this.setCenter(layoutContent);

//        Platform.runLater(() -> testContent.setVisible(false));
    }

    public static AudionPanel instance()
    {
        return new AudionPanel();
    }

    public AudionPanel inset(int dimension)
    {

        BorderSection.edgeSet().forEach(section ->
        {
            Rectangle rect = new Rectangle(dimension, dimension);

            section.place(this, rect);

            rect.setVisible(false);
        });

        return this;
    }
}
