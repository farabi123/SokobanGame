package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Box extends Space{
    public Box(int x, int y) {
        super(x, y);
    }

    void draw(Canvas c) {
        Paint boxx = new Paint();
        boxx.setColor(Color.BLACK);
        c.drawRect(x, y+50, x + 120, y + 140, boxx);
    }
    //These methods help determine the class type of an element in the game array
    boolean isFree() {return false;}
    boolean isBox(){return true;}
}