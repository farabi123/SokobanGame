package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by admin on 4/30/2016.
 */
public class DeliveryPointWithBox extends StorageSpot {

    public DeliveryPointWithBox(int x, int y) {
        super(x, y);
    }
    void draw(Canvas c) {
        Paint bo = new Paint();
        bo.setColor(Color.BLACK);
        c.drawRect(x, y+50, x + 125, y + 145, bo);
    }
    boolean isFree() {
        return false;
    }
    boolean isDeliveryPointWithBox(){
        return true;
    }
}
