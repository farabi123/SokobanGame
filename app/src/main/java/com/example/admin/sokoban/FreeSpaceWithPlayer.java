package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class FreeSpaceWithPlayer extends Space{
    int r;
    public FreeSpaceWithPlayer(int x, int y) {
        super(x, y);
        r = 45;
    }

    void draw(Canvas c)
    {
        Paint ball = new Paint();
        ball.setColor(Color.BLUE);
        c.drawCircle(x+70, y+80, r, ball);
    }
    //These methods help determine the class type of an element in the game array
    boolean isFree() {return false;}
    boolean isFreeSpaceWithPlayer(){return true;}
}


