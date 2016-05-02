package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall extends Space {
    public Wall(int x, int y) {
        super( x, y);
    }

    void draw(Canvas c) {
        Paint wall = new Paint();
        wall.setColor(Color.MAGENTA);
        c.drawRect(x, y+50, x + 125, y + 145, wall);
    }
    //These methods help determine the class type of an element in the game array
    boolean isFree() {
        return false;
    }
    boolean isWall(){
        return true;
    }
}