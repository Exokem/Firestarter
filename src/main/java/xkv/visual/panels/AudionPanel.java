package xkv.visual.panels;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import xkv.visual.controls.ButtonFactory;
import xkv.visual.controls.MultiButton;
import xkv.visual.css.IStylable;
import xkv.visual.css.PanelApplicators;
import xkv.visual.css.Style;

public class AudionPanel extends BorderPane implements IStylable
{
    private final GridPane layoutContent = PanelApplicators.inset(PanelFactory.paddedGrid(10, 5, 3, Style.INSET));

    private AudionPanel()
    {
        layoutContent.setPrefWidth(1024);
        layoutContent.setPrefHeight(760);

        StandardScrollPanel<MultiButton> albums = PanelFactory.multiButtonScrollPanel().width(200);

        albums.addVisualStyle(Style.BSP_BUTTON);

        MultiButton testAlbum = ButtonFactory.multiButton(0, 2, 3);
        albums.addItem(testAlbum);

        testAlbum.add(new Button("0"), 0, 0);
        testAlbum.add(new Button("0"), 0, 1);
        testAlbum.add(new Button("0"), 0, 2);
        testAlbum.add(new Button("Album Name"), 1, 0, 1, 3, true, true);

        layoutContent.add(albums, 1, 1);
        GridPane.setVgrow(albums, Priority.ALWAYS);
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
