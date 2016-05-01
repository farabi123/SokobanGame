package com.example.admin.sokoban;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by admin on 4/30/2016.
 */
public class DeliveryPointWithPlayer extends StorageSpot {
        int r;

        public DeliveryPointWithPlayer(int x, int y) {
            super(x, y);
            r = 45;
        }

        void draw(Canvas c)
        {
            Paint ball = new Paint();
            ball.setColor(Color.RED);
            c.drawCircle(x+70, y+80, r, ball);
        }

        boolean isFree() {
            return false;
        }
        boolean isSpot(){
           return false;
        }
        boolean isDeliveryPointWithPlayer(){
            return true;
        }
}
