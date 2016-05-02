package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*Wall, Box, StorageSpot, FreeSpaceWithPlayer,DeliveryPointWithPlayer,
DeliveryPointWithBox classes all extend Space class*/
public class Space {
    public int x, y;
    public Space(int x, int y) {
        this.x = x;
        this.y = y;
    }
    void draw(Canvas c) {
        Paint space = new Paint();
        space.setColor(Color.WHITE);
        c.drawRect(x, y+50, x + 125, y + 145, space);
    }
    //These methods help determine the class type of an element in the game array
    boolean isFree() {
        return true;
    }
    boolean isFreeSpaceWithPlayer(){
        return false;
    }
    boolean isWall(){
        return false;
    }
    boolean isBox(){
        return false;
    }
    boolean isSpot(){
        return false;
    }
    boolean isDeliveryPointWithPlayer(){
        return false;
    }
    boolean isDeliveryPointWithBox(){return false;}
}
