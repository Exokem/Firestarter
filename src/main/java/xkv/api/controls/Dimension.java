package xkv.api.controls;

import javafx.scene.layout.Region;

public enum Dimension
{
    PREF_WIDTH, PREF_HEIGHT, MIN_WIDTH, MIN_HEIGHT, MAX_WIDTH, MAX_HEIGHT;

    public void apply(Region region, double value)
    {
        switch (this)
        {
            case MIN_WIDTH: region.setMinWidth(value); break;
            case PREF_WIDTH: region.setPrefWidth(value); break;
            case MAX_WIDTH: region.setMaxWidth(value); break;
            case MIN_HEIGHT: region.setMinHeight(value); break;
            case PREF_HEIGHT: region.setPrefHeight(value); break;
            case MAX_HEIGHT: region.setMaxHeight(value); break;
        }
    }
}
