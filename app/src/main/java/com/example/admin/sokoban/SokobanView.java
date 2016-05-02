package com.example.admin.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


    // This is the ‘‘game engine ’ ’.
    public class SokobanView extends SurfaceView implements SurfaceHolder.Callback {
        public SokobanView(Context context) {
            super(context);
            // Notify the SurfaceHolder that you’d like to receive // SurfaceHolder callbacks .
            getHolder().addCallback(this);
            setFocusable(true);
        }

        SokobanThread st;
        int i, j;
        Space[][] game;
        Space player;
        float Dx, Dy;
        int level = 1;
        int countS=0;
        int countD=0;
        int x_r= 5*122;
        int y_r= 11*119;
        int x_l= 3*122;
        int y_l= 11*119;
        int x_u= 4*122;
        int y_u= 10*119;
        int x_d= 4*122;
        int y_d= 12*119;
        int box_to_x=0;
        int box_to_y=0;


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //Construct game initial level
            loadLevel(1);
            // Launch animator thread .
            st = new SokobanThread(this);
            st.start();
        }

        public void draw(Canvas c) {
            //Draw the canvas
            c.drawColor(Color.WHITE);
            //Draw the game

            for (i = 0; i < 8; i++) {
                for (j = 0; j < 13; j++) {
                    if (game[i][j] != null) {
                        game[i][j].draw(c);

                    }
                }
            }
            //Check if level is complete
            boolean complete=checkLevelComplete();
            //If level is complete move to the next level
            if(complete){
                level=level+1;
                loadLevel(level);
            }

        }//END OF DRAWING

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // SpaceThread regularly checks its interrupt flag.
            st.interrupt();
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            int action = e.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Dx = e.getX();
                    Dy = e.getY();
                    int indexX = player.x / 135;
                    int indexY = player.y / 119;
                    //Move method called here depending on which arrow pad is clicked
                    if (Dx >= x_r && Dx <= (x_r + 125) && Dy >= (y_r + 50) && Dy <= (y_r + 145)) {
                        System.out.println("CLICK RIGHT");
                        move(indexX, indexY, indexX + 1, indexY);
                    }
                    if (Dx >= x_l && Dx <= (x_l + 125) && Dy >= (y_l + 50) && Dy <= (y_l + 145)) {
                        System.out.println("CLICK LEFT");
                        move(indexX, indexY, indexX - 1, indexY);
                    }
                    if (Dx >= x_u && Dx <= (x_u + 125) && Dy >= (y_u + 50) && Dy <= (y_u + 145)) {
                        System.out.println("CLICK UP");
                        move(indexX, indexY, indexX, indexY - 1);
                    }
                    if (Dx >= x_d && Dx <= (x_d + 125) && Dy >= (y_d + 50) && Dy <= (y_d + 145)) {
                        System.out.println("CLICK DOWN");
                        move(indexX, indexY, indexX, indexY + 1);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //Do nothing
                    break;
            }
            return true;
        }
        //Move method body
        public void move(int from_x, int from_y, int to_x, int to_y) {
            //If the next position player is moving to is a walll then do nothing
            if (game[to_x][to_y].isWall()) {
                return;
            }
            //If the next position is a box or a box on a delivery point:
            if (game[to_x][to_y].isBox() || game[to_x][to_y].isDeliveryPointWithBox()) {
                //find the coordinates of the box's next position
                /*Moving up for indexes*/
                if (to_y < from_y) {
                    box_to_y = to_y - 1;
                    box_to_x = to_x;
                }
                /*Moving down for indexes*/
                if (to_y > from_y) {
                    box_to_y = to_y + 1;
                    box_to_x = to_x;
                }
                /*Moving left for indexes*/
                if (to_x < from_x) {
                    box_to_x = to_x - 1;
                    box_to_y = to_y;
                }
                /*Moving right for indexes*/
                if (to_x > from_x) {
                    box_to_x = to_x + 1;
                    box_to_y = to_y;
                }
                // Can't move a box of the box's next location is a wall, box or a box on a delivery point
                if (game[box_to_x][box_to_y].isBox() || game[box_to_x][box_to_y].isDeliveryPointWithBox()
                        || game[box_to_x][box_to_y].isWall()) {
                    return;
                }
                //Making new instances for the game array objects depending on what location the player and box leave and move to
                if (game[from_x][from_y].isFreeSpaceWithPlayer()) {
                    game[from_x][from_y] = new Space(from_x * 135, from_y * 119);
                }
                if (game[from_x][from_y].isDeliveryPointWithPlayer()) {
                    game[from_x][from_y] = new StorageSpot(from_x * 135, from_y * 119);
                }
                if (game[to_x][to_y].isBox()) {
                    game[to_x][to_y] = new FreeSpaceWithPlayer(to_x * 135, to_y * 119);
                    player = game[to_x][to_y];
                }
                if (game[to_x][to_y].isDeliveryPointWithBox()) {
                    game[to_x][to_y] = new DeliveryPointWithPlayer(to_x * 135, to_y * 119);
                    player = game[to_x][to_y];
                }
                if (game[box_to_x][box_to_y].isFree()) {
                    game[box_to_x][box_to_y] = new Box(box_to_x * 135, box_to_y * 119);
                }
                if (game[box_to_x][box_to_y].isSpot()) {
                    game[box_to_x][box_to_y] = new DeliveryPointWithBox(box_to_x * 135, box_to_y * 119);
                }
            }
            //Making new instances for the game array objects depending on what location the player leaves and moves to
            if (game[to_x][to_y].isFree()) {
                game[to_x][to_y] = new FreeSpaceWithPlayer(to_x * 135, to_y * 119);
                player = game[to_x][to_y];
            }
            if (game[to_x][to_y].isSpot()) {
                game[to_x][to_y] = new DeliveryPointWithPlayer(to_x * 135, to_y * 119);
                player = game[to_x][to_y];
            }

            if (game[from_x][from_y].isFreeSpaceWithPlayer()) {
                game[from_x][from_y] = new Space(from_x * 135, from_y * 119);
            }
            if (game[from_x][from_y].isDeliveryPointWithPlayer()) {
                game[from_x][from_y] = new StorageSpot(from_x * 135, from_y * 119);
            }

        }
        //Loading levels method body
        public void loadLevel(int level) {
            switch (level) {
                //LEVEL 1
                case 1:
                    game = new Space[8][13];
                    //Make the player
                    game[5][1] = new FreeSpaceWithPlayer(5 * 135, 1 * 119);
                    player = game[5][1];
                    //Make the empty spaces
                    for (i = 1; i < 4; i++) {
                        for (j = 1; j < 4; j++) {
                            game[i][j] = new Space(i * 135, j * 119);
                        }
                    }
                    i = 6;
                    for (j = 1; j < 6; j++) {
                        game[i][j] = new Space(i * 135, j * 119);
                    }
                    game[5][3] = new Space(5 * 135, 3 * 119);
                    game[5][5] = new Space(5 * 135, 5 * 119);
                    game[4][2] = new Space(4 * 135, 2 * 119);
                    game[4][3] = new Space(4 * 135, 3 * 119);
                    // Make the Arrow pad
                    game[4][10] = new Wall(4 * 122, 10 * 119);
                    game[3][11] = new Wall(3 * 122, 11 * 119);
                    game[5][11] = new Wall(5 * 122, 11 * 119);
                    game[4][12] = new Wall(4 * 122, 12 * 119);
                    //Make the spots
                    game[6][4] = new StorageSpot(6 * 135, 4 * 119);
                    game[6][5] = new StorageSpot(6 * 135, 5 * 119);
                    //Make the Box
                    game[5][2] = new Box(5 * 135, 2 * 119);
                    game[5][4] = new Box(5 * 135, 4 * 119);
                    //Make the Wall
                    game[4][1] = new Wall(4 * 135, 1 * 119);
                    game[4][5] = new Wall(4 * 135, 5 * 119);

                    i = 0;
                    for (j = 0; j < 5; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    i = 7;
                    for (j = 0; j < 7; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 0;
                    for (i = 1; i < 7; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 4;
                    for (i = 1; i < 5; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 6;
                    for (i = 4; i < 8; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    break;
                //LEVEL 2
                case 2:
                    game = new Space[8][13];
                    //Make the player
                    game[5][6] = new FreeSpaceWithPlayer(5 * 135, 6 * 119);
                    player = game[5][6];
                    //Make the empty spaces
                    for (i = 1; i < 4; i++) {
                        for (j = 1; j < 6; j++) {
                            game[i][j] = new Space(i * 135, j * 119);
                        }
                    }
                    game[4][4] = new Space(4 * 135, 4 * 119);
                    game[4][3] = new Space(4 * 135, 3 * 119);
                    game[5][1] = new Space(5 * 135, 1 * 119);
                    game[5][5] = new Space(5 * 135, 5 * 119);
                    game[6][1] = new Space(6 * 135, 1 * 119);
                    game[6][5] = new Space(6 * 135, 5 * 119);
                    game[6][6] = new Space(6 * 135, 6 * 119);
                    // Make the arrow pad
                    game[4][10] = new Wall(4 * 122, 10 * 119);
                    game[3][11] = new Wall(3 * 122, 11 * 119);
                    game[5][11] = new Wall(5 * 122, 11 * 119);
                    game[4][12] = new Wall(4 * 122, 12 * 119);
                    //Make the storage spots
                    game[6][2] = new StorageSpot(6 * 135, 2 * 119);
                    game[6][3] = new StorageSpot(6 * 135, 3 * 119);
                    game[6][4] = new StorageSpot(6 * 135, 4 * 119);
                    //Make the boxes
                    game[5][2] = new Box(5 * 135, 2 * 119);
                    game[5][3] = new Box(5 * 135, 3 * 119);
                    game[5][4] = new Box(5 * 135, 4 * 119);
		            /*Make all the vertical walls*/
                    i = 0;
                    for (j = 1; j < 6; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    i = 7;
                    for (j = 1; j < 7; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
		            /*Make all the random walls*/
                    game[4][1] = new Wall(4 * 135, 1 * 119);
                    game[4][2] = new Wall(4 * 135, 2 * 119);
                    //game[4][4] = new Wall(4 * 135, 4 * 119);
                    game[4][5] = new Wall(4 * 135, 5 * 119);
                    /*Make all the horizontal walls*/
                    j = 0;
                    for (i = 0; i < 8; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 6;
                    for (i = 0; i < 5; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 7;
                    for (i = 4; i < 8; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    break;
                //LEVEL 3
                case 3:
                    game = new Space[8][13];

                    game[2][2] = new FreeSpaceWithPlayer(2 * 135, 2 * 119);
                    player = game[2][2];

                    i = 2;
                    for (j = 4; j < 8; j++) {
                        game[i][j] = new Space(i * 135, j * 119);
                    }
                    game[1][5] = new Space(1 * 135, 5 * 119);
                    game[3][5] = new Space(3 * 135, 5 * 119);
                    game[3][7] = new Space(3 * 135, 7 * 119);

                    i = 4;
                    for (j = 1; j < 4; j++) {
                        game[i][j] = new Space(i * 135, j * 119);
                    }

                    i = 4;
                    for (j = 5; j < 8; j++) {
                        game[i][j] = new Space(i * 135, j * 119);
                    }
                    game[5][1] = new Space(5 * 135, 1 * 119);
                    i = 5;
                    for (j = 3; j < 7; j++) {
                        game[i][j] = new Space(i * 135, j * 119);
                    }
                    i = 6;
                    for (j = 1; j < 4; j++) {
                        game[i][j] = new Space(i * 135, j * 119);
                    }
                    i = 6;
                    for (j = 5; j < 8; j++) {
                        game[i][j] = new Space(i * 135, j * 119);
                    }
                    /*Arrow pad*/
                    game[4][10] = new Wall(4 * 122, 10 * 119);
                    game[3][11] = new Wall(3 * 122, 11 * 119);
                    game[5][11] = new Wall(5 * 122, 11 * 119);
                    game[4][12] = new Wall(4 * 122, 12 * 119);
                    //Make the storage spots
                    game[1][6] = new StorageSpot(1 * 135, 6 * 119);
                    game[3][6] = new StorageSpot(3 * 135, 6 * 119);
                    game[5][7] = new StorageSpot(5 * 135, 7 * 119);
                    // Make the boxes
                    game[2][3] = new Box(2 * 135, 3 * 119);
                    game[3][2] = new Box(3 * 135, 2 * 119);
                    game[3][3] = new Box(3 * 135, 3 * 119);
		            /*Make random walls*/
                    game[3][4] = new Wall(3 * 135, 4 * 119);
                    game[4][4] = new Wall(4 * 135, 4 * 119);
                    game[5][2] = new Wall(5 * 135, 2 * 119);
                    game[6][4] = new Wall(6 * 135, 4 * 119);
		            /*Make all the vertical walls*/
                    i = 0;
                    for (j = 4; j < 8; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    i = 1;
                    for (j = 1; j < 5; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    i = 1;
                    for (j = 7; j < 9; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    i = 7;
                    for (j = 0; j < 9; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
		            /*Make all the horizontal walls*/
                    j = 0;
                    for (i = 3; i < 8; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 1;
                    for (i = 2; i < 4; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 8;
                    for (i = 2; i < 8; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 8;
                    for (i = 1; i < 7; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    break;
                //LEVEL 4
                case 4:
                    game = new Space[8][13];
                    //Make the player
                    game[2][2] = new FreeSpaceWithPlayer(2 * 135, 2 * 119);
                    player = game[2][2];
                    // Make the empty spaces
                    game[1][5] = new Space(1 * 135, 5 * 119);
                    game[1][7] = new Space(1 * 135, 7 * 119);
                    game[2][6] = new Space(2 * 135, 6 * 119);
                    game[2][7] = new Space(2 * 135, 7 * 119);
                    game[3][1] = new Space(3 * 135, 1 * 119);
                    game[3][3] = new Space(3 * 135, 3 * 119);
                    game[3][5] = new Space(3 * 135, 5 * 119);
                    game[3][7] = new Space(3 * 135, 7 * 119);
                    game[4][1] = new Space(4 * 135, 1 * 119);
                    game[4][2] = new Space(4 * 135, 2 * 119);
                    game[5][1] = new Space(5 * 135, 1 * 119);
                    game[5][2] = new Space(5 * 135, 2 * 119);
                    game[5][4] = new Space(5 * 135, 4 * 119);
                    game[5][5] = new Space(5 * 135, 5 * 119);
                    game[5][7] = new Space(5 * 135, 7 * 119);
                    game[6][7] = new Space(6 * 135, 7 * 119);
                    // Make the arrow pad
                    game[4][10] = new Wall(4 * 122, 10 * 119);
                    game[3][11] = new Wall(3 * 122, 11 * 119);
                    game[5][11] = new Wall(5 * 122, 11 * 119);
                    game[4][12] = new Wall(4 * 122, 12 * 119);
                    //Make the Storage spots
                    game[1][2] = new StorageSpot(1 * 135, 2 * 119);
                    game[1][4] = new StorageSpot(1 * 135, 4 * 119);
                    game[3][6] = new StorageSpot(3 * 135, 6 * 119);
                    game[4][5] = new StorageSpot(4 * 135, 5 * 119);
                    game[4][7] = new StorageSpot(4 * 135, 7 * 119);
                    game[5][3] = new StorageSpot(5 * 135, 3 * 119);
                    game[6][6] = new StorageSpot(6 * 135, 6 * 119);
                    //Make the boxes
                    game[3][2] = new Box(3 * 135, 2 * 119);
                    game[4][3] = new Box(4 * 135, 3 * 119);
                    game[4][4] = new Box(4 * 135, 4 * 119);
                    game[1][6] = new Box(1 * 135, 6 * 119);
                    game[4][6] = new Box(4 * 135, 6 * 119);
                    game[5][6] = new Box(5 * 135, 6 * 119);
                    //Make delivery point with box
                    game[3][6] = new DeliveryPointWithBox(3 * 135, 6 * 119);
		            /*Make random wall blocks*/
                    game[1][3] = new Wall(1 * 135, 3 * 119);
                    game[2][3] = new Wall(2 * 135, 3 * 119);
                    game[2][4] = new Wall(2 * 135, 4 * 119);
                    game[2][5] = new Wall(2 * 135, 5 * 119);
                    game[3][4] = new Wall(3 * 135, 4 * 119);
		            /*Make all the vertical walls*/
                    i = 0;
                    for (j = 1; j < 9; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    i = 6;
                    for (j = 0; j < 6; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    i = 7;
                    for (j = 5; j < 9; j++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
		            /*Make all the horizontal walls*/
                    j = 0;
                    for (i = 2; i < 7; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 1;
                    for (i = 1; i < 3; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    j = 8;
                    for (i = 1; i < 7; i++) {
                        game[i][j] = new Wall(i * 135, j * 119);
                    }
                    break;
                case 5:
                    game = new Space[8][13];
                    for (i = 0; j < 8; i++){
                        for (j = 0; j < 13; j++) {
                            game[i][j] = new Space(i * 135, j * 119);
                        }
            }
            }
        }

        //Check if level is complete method bode
        public boolean checkLevelComplete(){
            countS=0;
            countD=0;
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 13; j++) {
                    if (game[i][j] != null) {
                        if (game[i][j].isDeliveryPointWithBox()) {
                            countD++;
                        }
                        if (game[i][j].isSpot()||game[i][j].isDeliveryPointWithPlayer()) {
                            countS++;
                        }
                    }
                }
            }
            if (countD == countS) {
                return true;
            }
        return false;
        }
    }

