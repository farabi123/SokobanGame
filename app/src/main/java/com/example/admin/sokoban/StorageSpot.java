package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class StorageSpot extends Space {
    int r;
    public StorageSpot(int x, int y) {
        super( x, y);
        r=25;
    }
    void draw(Canvas c)
    {
        Paint ball = new Paint();
        ball.setColor(Color.RED);
        c.drawCircle(x+70, y+80, r, ball);
    }
    //These methods help determine the class type of an element in the game array
    boolean isFree() {
        return false;
    }
    boolean isSpot(){
        return true;
    }
}
