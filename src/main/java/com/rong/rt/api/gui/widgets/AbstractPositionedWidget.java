package com.rong.rt.api.gui.widgets;

import com.rong.rt.api.gui.Widget;

public class AbstractPositionedWidget extends Widget {

    protected int xPosition;
    protected int yPosition;

    public AbstractPositionedWidget(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}
