package com.example.tetris;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Figure {

    ArrayList<Piece> pieces = new ArrayList();
    Random r = new Random();
    int xOrigin;
    int color;

    public Figure(int cols){

        color = getColor();
        xOrigin = cols / 2;
        getFigure();

    }

    public int getColor(){

        return Color.rgb(50 + r.nextInt(205),50 + r.nextInt(205),50+ r.nextInt(205));

    }

    public void getFigure(){

        int type = r.nextInt(7);

        switch (type){
            case 0:
                //square
                pieces.add(new Piece(xOrigin + 1, 1));
                pieces.add(new Piece(xOrigin + 1, 0));
                pieces.add(new Piece(xOrigin, 1));
                pieces.add(new Piece(xOrigin, 0));
                break;

            case 1:
                //line
                pieces.add(new Piece(xOrigin, 0));
                pieces.add(new Piece(xOrigin + 1, 0));
                pieces.add(new Piece(xOrigin + 2, 0));
                pieces.add(new Piece(xOrigin + 3, 0));
                break;
            case 2:
                //l
                pieces.add(new Piece(xOrigin, 0));
                pieces.add(new Piece(xOrigin, 1));
                pieces.add(new Piece(xOrigin + 1, 1));
                pieces.add(new Piece(xOrigin + 2, 1));
                break;
            case 3:
                //t
                pieces.add(new Piece(xOrigin, 0));
                pieces.add(new Piece(xOrigin + 1, 1));
                pieces.add(new Piece(xOrigin, 1));
                pieces.add(new Piece(xOrigin - 1, 1));
                break;
            case 4:
                //z
                pieces.add(new Piece(xOrigin, 0));
                pieces.add(new Piece(xOrigin + 1, 0));
                pieces.add(new Piece(xOrigin, 1));
                pieces.add(new Piece(xOrigin - 1, 1));
                break;
            case 5:
                //!l
                pieces.add(new Piece(xOrigin, 0));
                pieces.add(new Piece(xOrigin, 1));
                pieces.add(new Piece(xOrigin - 1, 1));
                pieces.add(new Piece(xOrigin - 2, 1));
                break;
            case 6:
                //!z
                pieces.add(new Piece(xOrigin, 0));
                pieces.add(new Piece(xOrigin + 1, 0));
                pieces.add(new Piece(xOrigin + 1, 1));
                pieces.add(new Piece(xOrigin + 2, 1));
                break;
        }


    }

    public void rotate() {

        Piece pivot = pieces.get(2);
        Piece rotated = new Piece(0,0);
        int tmpX;

        for(Piece p: pieces){
            if(p != pivot){
                rotated.x = p.x - pivot.x;
                rotated.y = p.y - pivot.y;

                tmpX = rotated.x * 0 + rotated.y * -1;
                rotated.y = rotated.x * 1 + rotated.y * 0;
                rotated.x = tmpX;

                p.x = pivot.x + rotated.x;
                p.y = pivot.y + rotated.y;
            }
        }
    }
}
