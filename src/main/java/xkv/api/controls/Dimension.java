package xkv.api.controls;

import javafx.scene.layout.Region;

public enum Dimension
{
    PREF_WIDTH, PREF_HEIGHT, MIN_WIDTH, MIN_HEIGHT, MAX_WIDTH, MAX_HEIGHT;

    public void apply(Region region, double value)
    {
        switch (this)
        {
            case MIN_WIDTH -> region.setMinWidth(value);
            case PREF_WIDTH -> region.setPrefWidth(value);
            case MAX_WIDTH -> region.setMaxWidth(value);
            case MIN_HEIGHT -> region.setMinHeight(value);
            case PREF_HEIGHT -> region.setPrefHeight(value);
            case MAX_HEIGHT -> region.setMaxHeight(value);
        }
    }
}
