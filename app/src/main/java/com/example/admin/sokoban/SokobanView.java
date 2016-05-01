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
            //put here what was discussed, loading levels
            //load level one in the constructor

            setFocusable(true);
        }

        SokobanThread st;
        int i, j;
        Space[][] game;
        Space player;
        float Dx, Dy;
        int level=0;

        int count=0;
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //Construct game initial state .
            // Launch animator thread .
            // Allocating memory for called instance

            System.out.println("level top="+level);
            switch(level) {
                case 0:
                    loadLevel0();
                    break;
                case 1:
                    loadLevel1();
                    break;
                case 2:
                    loadLevel2();
                    break;
                case 3:
                    loadLevel3();
                    break;
                case 4:
                    loadLevel4();
                    break;
            }
            if(game[6][5].isDeliveryPointWithBox()){
                System.out.println("its done!!!!!!!!!!!!!!!!!!");
                level=1;
            }
            st = new SokobanThread(this);
            st.start();
        }

        public void draw(Canvas c) {
            //---------------------------------Draw the canvas
            c.drawColor(Color.WHITE);
            //---------------------------------Draw the player


            for (i = 0; i < 8; i++) {
                for (j = 0; j < 13; j++) {
                    if (game[i][j] != null) {
                        game[i][j].draw(c);

                    }
                }
            }

        }//END OF DRAWING


        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Respond to surface changes , e.g. ,
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // The cleanest way to stop a thread is by interrupting it.
            // SpaceThread regularly checks its interrupt flag.
            st.interrupt();
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {

            int action = e.getAction();
            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    System.out.println("CLICK DOWN");
                    Dx = e.getX();
                    Dy = e.getY();
                    int indexX=player.x/135;
                    int indexY=player.y/119;

                    if(Dx >= x_r && Dx <= (x_r+125) && Dy >=(y_r+50) && Dy <=(y_r+145)) {
                        System.out.println("CLICK RIGHT");
                        move(indexX,indexY, indexX+1,indexY );
                    }
                    if(Dx >= x_l && Dx <= (x_l+125) && Dy >=(y_l+50) && Dy <=(y_l+145)) {
                        System.out.println("CLICK LEFT");
                        move(indexX,indexY, indexX-1,indexY );
                    }
                    if(Dx >= x_u && Dx <= (x_u+125) && Dy >=(y_u+50) && Dy <=(y_u+145)) {
                        System.out.println("CLICK UP");
                        move(indexX,indexY, indexX,indexY-1 );
                    }
                    if(Dx >= x_d && Dx <= (x_d+125) && Dy >=(y_d+50) && Dy <=(y_d+145)) {
                        System.out.println("CLICK DOWN");
                        move(indexX,indexY, indexX,indexY+1 );
                    }
            break;
            case MotionEvent.ACTION_UP:
            break;
        }
//check if deliv ptbox
            //level=2;

            return true;
        }

        public void move(int from_x, int from_y, int to_x, int to_y) {
            if (game[to_x][to_y].isWall()) {
                return;
            }

            if(game[to_x][to_y].isBox() || game[to_x][to_y].isDeliveryPointWithBox()) {

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
                if(game[box_to_x][box_to_y].isBox()||game[box_to_x][box_to_y].isDeliveryPointWithBox()||game[box_to_x][box_to_y].isWall()){
                    return;
                }
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

            if(game[to_x][to_y].isFree()) {
                game[to_x][to_y] = new FreeSpaceWithPlayer(to_x * 135, to_y * 119);
                player = game[to_x][to_y];
            }
            if(game[to_x][to_y].isSpot()){
                game[to_x][to_y] = new DeliveryPointWithPlayer(to_x * 135, to_y * 119);
                player = game[to_x][to_y];

            }

            System.out.println("class name"+game[from_x][from_y].getClass().toString());
            if(game[from_x][from_y].isFreeSpaceWithPlayer()){
                game[from_x][from_y] = new Space(from_x * 135, from_y * 119);
            }

            if(game[from_x][from_y].isDeliveryPointWithPlayer()) {
                System.out.println("deliveryPointWithPlayer");
                game[from_x][from_y] = new StorageSpot(from_x * 135, from_y * 119);
            }

        }

        public void loadLevel0(){
            game = new Space[8][13];

            game[5][1] = new FreeSpaceWithPlayer(5 * 135, 1 * 119);
            player = game[5][1];
            for (i = 1; i < 7; i++) {
                for (j = 1; j < 7; j++) {
                    game[i][j] = new Space(i * 135, j * 119);
                }
            }
                    /*Arrow pad*/
            game[4][10] = new Wall(4 * 122, 10 * 119);
            game[3][11] = new Wall(3 * 122, 11 * 119);
            game[5][11] = new Wall(5 * 122, 11 * 119);
            game[4][12] = new Wall(4 * 122, 12 * 119);
            game[5][2] = new Box(5 * 135, 2 * 119);
            game[6][5] = new StorageSpot(6 * 135, 5 * 119);
            i = 0;
            for (j = 0; j < 7; j++) {
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
            j = 7;
            for (i = 0; i < 8; i++) {
                game[i][j] = new Wall(i * 135, j * 119);
            }

        }
public void loadLevel1(){
    game = new Space[8][13];

    game[5][1] = new FreeSpaceWithPlayer(5 * 135, 1 * 119);
    player = game[5][1];

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
            /*Arrow pad*/
    game[4][10] = new Wall(4 * 122, 10 * 119);
    game[3][11] = new Wall(3 * 122, 11 * 119);
    game[5][11] = new Wall(5 * 122, 11 * 119);
    game[4][12] = new Wall(4 * 122, 12 * 119);

    game[6][4] = new StorageSpot(6 * 135, 4 * 119);
    game[6][5] = new StorageSpot(6 * 135, 5 * 119);

    game[5][2] = new Box(5 * 135, 2 * 119);
    game[5][4] = new Box(5 * 135, 4 * 119);

    game[4][1] = new Wall(4 * 135, 1 * 119);
    game[4][3] = new Wall(4 * 135, 3 * 119);
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
}
        public void loadLevel2(){
            game = new Space[8][13];

            game[5][6] = new FreeSpaceWithPlayer(5 * 135, 6 * 119);
            player = game[5][6];
            for (i = 1; i < 4; i++) {
                for (j = 1; j < 6; j++) {
                    game[i][j] = new Space(i * 135, j * 119);
                }
            }
            game[4][3] = new Space(4 * 135, 3 * 119);
            game[5][1] = new Space(5 * 135, 1 * 119);
            game[5][5] = new Space(5 * 135, 5 * 119);
            game[6][1] = new Space(6 * 135, 1 * 119);
            game[6][5] = new Space(6 * 135, 5 * 119);
            game[6][6] = new Space(6 * 135, 6 * 119);
                    /*Arrow pad*/
            game[4][10] = new Wall(4 * 122, 10 * 119);
            game[3][11] = new Wall(3 * 122, 11 * 119);
            game[5][11] = new Wall(5 * 122, 11 * 119);
            game[4][12] = new Wall(4 * 122, 12 * 119);
                    /*Make the storage spot*/
            game[6][2] = new StorageSpot(6*135,2*119);
            game[6][3] = new StorageSpot(6*135,3*119);
            game[6][4] = new StorageSpot(6*135,4*119);
		/*Make the box*/
            game[5][2] = new Box(5*135,2*119);
            game[5][3] = new Box(5*135,3*119);
            game[5][4] = new Box(5*135,4*119);
		/*Make all the vertical walls*/
            i = 0;
            for (j = 1; j < 6; j++) {
                game[i][j] = new Wall(i*135, j*119);
            }
            i = 7;
            for (j = 1; j < 7; j++) {
                game[i][j] = new Wall(i*135, j*119);
            }
		/*Make all the random walls*/

            game[4][1] = new Wall(4*135, 1*119);
            game[4][2] = new Wall(4*135, 2*119);
            game[4][4] = new Wall(4*135, 4*119);
            game[4][5] = new Wall(4*135, 5*119);
		/*Make all the horizontal walls*/
            j = 0;
            for (i = 0 ; i < 8; i++) {
                game[i][j] = new Wall( i*135, j*119);
            }

            j = 6;
            for (i = 0 ; i < 5; i++) {
                game[i][j] = new Wall( i*135, j*119);
            }

            j = 7;
            for (i = 4 ; i < 8; i++) {
                game[i][j] = new Wall( i*135, j*119);
            }

        }
        public void loadLevel3(){game = new Space[8][13];

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
            i=6;
            for (j = 1; j < 4; j++) {
                game[i][j] = new Space(i * 135, j * 119);
            }
            i=6;
            for (j = 5; j < 8; j++) {
                game[i][j] = new Space(i * 135, j * 119);
            }
                    /*Arrow pad*/
            game[4][10] = new Wall(4 * 122, 10 * 119);
            game[3][11] = new Wall(3 * 122, 11 * 119);
            game[5][11] = new Wall(5 * 122, 11 * 119);
            game[4][12] = new Wall(4 * 122, 12 * 119);
            game[1][6] = new StorageSpot(1*135,6*119);
            game[3][6] = new StorageSpot(3*135,6*119);
            game[5][7] = new StorageSpot(5*135,7*119);

            game[2][3] = new Box(2*135,3*119);
            game[3][2] = new Box(3*135,2*119);
            game[3][3] = new Box(3*135,3*119);

		/*Make random walls*/
            game[3][4] = new Wall(3*135, 4*119);
            game[4][4] = new Wall(4*135, 4*119);
            game[5][2] = new Wall(5*135, 2*119);
            game[6][4] = new Wall(6*135, 4*119);

		/*Make all the vertical walls*/
            i = 0;
            for (j = 4; j < 8; j++) {
                game[i][j] = new Wall( i*135, j*119);
            }
            i = 1;
            for (j = 1; j < 5; j++) {
                game[i][j] = new Wall(i*135, j*119);
            }
            i = 1;
            for (j = 7; j < 9; j++) {
                game[i][j] = new Wall(i*135, j*119);
            }
            i = 7;
            for (j = 0; j < 9; j++) {
                game[i][j] = new Wall(i*135, j*119);
            }
		/*Make all the horizontal walls*/
            j = 0;
            for (i = 3 ; i < 8; i++) {
                game[i][j] = new Wall(i*135, j*119);
            }

            j = 1;
            for (i = 2 ; i < 4; i++) {
                game[i][j] = new Wall(i*135, j*119);
            }

            j = 8;
            for (i = 2 ; i < 8; i++) {
                game[i][j] = new Wall(i*135, j*119);
            }

            j = 8;
            for (i = 1 ; i < 7; i++) {
                game[i][j] = new Wall(i*135, j*119);
            }
        }
        public void loadLevel4() {
            game = new Space[8][13];

            game[2][2] = new FreeSpaceWithPlayer(2 * 135, 2 * 119);
            player = game[2][2];

            game[1][5] = new Space( 1* 135,  5* 119);
            game[1][7] = new Space( 1* 135,  7* 119);
            game[2][6] = new Space( 2* 135,  6* 119);
            game[2][7] = new Space( 2* 135,  7* 119);
            game[3][1] = new Space( 3* 135,  1* 119);
            game[3][3] = new Space( 3* 135,  3* 119);
            game[3][5] = new Space( 3* 135,  5* 119);
            game[3][7] = new Space( 3* 135,  7* 119);
            game[4][1] = new Space( 4* 135,  1* 119);
            game[4][2] = new Space( 4* 135,  2* 119);
            game[5][1] = new Space( 5* 135,  1* 119);
            game[5][2] = new Space( 5* 135,  2* 119);
            game[5][4] = new Space( 5* 135,  4* 119);
            game[5][5] = new Space( 5* 135,  5* 119);
            game[5][7] = new Space( 5* 135,  7* 119);
            game[6][7] = new Space( 6* 135,  7* 119);

                    /*Arrow pad*/
            game[4][10] = new Wall(4 * 122, 10 * 119);
            game[3][11] = new Wall(3 * 122, 11 * 119);
            game[5][11] = new Wall(5 * 122, 11 * 119);
            game[4][12] = new Wall(4 * 122, 12 * 119);


            game[1][2] = new StorageSpot(1*135,2*119);
            game[1][4] = new StorageSpot(1*135,4*119);
            game[3][6] = new StorageSpot(3*135,6*119);
            game[4][5] = new StorageSpot(4*135,5*119);
            game[4][7] = new StorageSpot(4*135,7*119);
            game[5][3] = new StorageSpot(5*135,3*119);
            game[6][6] = new StorageSpot(6*135,6*119);

            game[3][2] = new Box(3*135,2*119);
            game[4][3] = new Box(4*135,3*119);
            game[4][4] = new Box(4*135,4*119);
            game[1][6] = new Box(1*135,6*119);
            game[3][6] = new Box(3*135,6*119);
            game[4][6] = new Box(4*135,6*119);
            game[5][6] = new Box(5*135,6*119);

		/*Make random blocks*/
            game[1][3] = new Wall(1*135, 3*119);
            game[2][3] = new Wall(2*135, 3*119);
            game[2][4] = new Wall(2*135, 4*119);
            game[2][5] = new Wall(2*135, 5*119);
            game[3][4] = new Wall(3*135, 4*119);

		/*Make all the vertical walls*/
            i = 0;
            for (j = 1; j < 9; j++) {
                game[i][j] = new Wall( i*135, j*119);
            }
            i = 6;
            for (j = 0; j < 6; j++) {
                game[i][j] = new Wall( i*135, j*119);
            }
            i = 7;
            for (j = 5; j < 9; j++) {
                game[i][j] = new Wall( i*135, j*119);
            }
		/*Make all the horizontal walls*/
            j = 0;
            for (i = 2 ; i < 7; i++) {
                game[i][j] = new Wall( i*135, j*119);
            }

            j = 1;
            for (i = 1 ; i < 3; i++) {
                game[i][j] = new Wall( i*135, j*119);
            }

            j = 8;
            for (i = 1 ; i < 7; i++) {
                game[i][j] = new Wall(i*135, j*119);
            }
                }
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
    }

