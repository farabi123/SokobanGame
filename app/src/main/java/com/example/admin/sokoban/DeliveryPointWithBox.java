package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DeliveryPointWithBox extends StorageSpot {
    public DeliveryPointWithBox(int x, int y) {
        super(x, y);
    }

    void draw(Canvas c) {
        Paint bo = new Paint();
        bo.setColor(Color.BLACK);
        c.drawRect(x, y+50, x + 125, y + 145, bo);
    }
    //These methods help determine the class type of an element in the game array
    boolean isFree() {
        return false;
    }
    boolean isDeliveryPointWithBox(){
        return true;
    }
}
