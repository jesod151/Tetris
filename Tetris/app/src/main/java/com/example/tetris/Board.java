package com.example.tetris;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class Board extends View {

    int rows = 21, cols = 12;
    int list[][];
    ArrayList<Figure> figures = new ArrayList();
    Figure current = new Figure(cols);

    Handler handler;
    Runnable runnable;
    final int UPDATE = 225;

    Paint paint = new Paint();
    float scaleI, scaleJ, paddingI, paddingJ;

    int frameReactionDelayMove = 1;
    int frameReactionDelayRot = 2;
    int frameCounterMove = frameReactionDelayMove, frameCounterRot = frameReactionDelayRot;
    int score = 0;

    public Board(Context context) {
        super(context);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    protected void onDraw(Canvas pCanvas){
        super.onDraw(pCanvas);

        handler.postDelayed(runnable, UPDATE);

        if(frameCounterMove < frameReactionDelayMove){
            frameCounterMove++;
        }

        if(frameCounterRot < frameReactionDelayRot){
            frameCounterRot++;
        }

        createBoard();
        setFigurePositions();


        if(isTouching(current, 1)){
            figures.add(current);
            for (Piece p: current.pieces) {
                if(p.y == 0){
                    resetGame();
                }
                list[p.y][p.x] = current.color;
            }
            current = new Figure(cols);
            deleteCompletes(getCompleteLines());
        }
        else{
            for (Piece p: current.pieces) {
                p.y++;
                list[p.y][p.x] = current.color;
            }
        }

        scaleI = pCanvas.getHeight() / rows;
        scaleJ = pCanvas.getWidth() / cols;
        paddingI = scaleI / 20;
        paddingJ = scaleJ / 20;

        paint.setColor(Color.BLACK);
        pCanvas.drawRect(0,0, pCanvas.getWidth(), pCanvas.getHeight(), paint);

        for(int i = 0; i < rows; i++){

            for(int j = 0; j < cols; j++) {

                if(list[i][j] == 0) {
                    paint.setColor(Color.BLACK);
                }
                else if(list[i][j] == -1){
                    paint.setColor(Color.GRAY);
                }
                else{
                    paint.setColor(list[i][j]);
                }
                pCanvas.drawRect(j * scaleJ + paddingJ, i * scaleI + paddingI, j * scaleJ + scaleJ - paddingJ, i * scaleI + scaleI - paddingI, paint);
            }
        }

        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        pCanvas.drawText(Integer.toString(score), pCanvas.getWidth() / 2, pCanvas.getHeight() / 10, paint);
    }

    private boolean isTouching(Figure f, int move){

        for(Piece p: f.pieces){

            if(!isInBoundaries(p.x, p.y + move)){
                return true;
            }
            else if (list[p.y + move][p.x] != 0 && list[p.y + move][p.x] != f.color) {
                return true;
            }

        }
        return false;

    }

    private boolean isInBoundaries(int x, int y){

        if(x >= 0 && x <= cols - 2 && y >= 1 && y <= rows - 2) {
            return true;
        }
        return false;

    }

    private ArrayList<Integer> getCompleteLines() {

        boolean complete;

        ArrayList<Integer> completes = new ArrayList<>();

        for(int i = rows - 2; i >= 0; i--){
            complete  = true;
            for(int j = 1; j < cols - 1; j++) {
                if(list[i][j] == 0){
                    complete = false;
                }
            }
            if(complete){
                score++;
                completes.add(i);
            }
        }

        return completes;

    }

    private void deleteCompletes(ArrayList<Integer> completes){

        if(completes.size() > 0){
            if(score%5 == 0){
                changeColors();
            }

            Piece temporal;
            int linesUnderPieceCounter;

            for(int f = 0; f < figures.size(); f++){
                for(int piece = 0; piece < figures.get(f).pieces.size(); piece++) {

                    temporal = figures.get(f).pieces.get(piece);

                    if(completes.contains(temporal.y)){
                        temporal.alive = false;
                    }
                    else if(temporal.alive){
                        linesUnderPieceCounter = 0;
                        for(int i = 0; i < completes.size(); i++){
                            if(temporal.y < completes.get(i)){
                                linesUnderPieceCounter++;
                            }
                        }
                        temporal.y += linesUnderPieceCounter;
                    }
                }
            }

        }

    }

    private void changeColors() {
        for(int f = 0; f < figures.size(); f++){
            figures.get(f).color = figures.get(f).getColor();
        }
    }

    public void moveCurrentManager(float x, float y){
        float offset = this.getWidth() / 3;
        if(x < offset){
            moveCurrent(-1);
        }
        else if(x > 2*offset){
            moveCurrent(1);
        }

        if(offset < x && x < 2*offset){
            makeRotation();
        }

        if(y < this.getHeight() / 4){
            resetGame();
        }

    }

    public void moveCurrent(int dir){

        if(frameCounterMove == frameReactionDelayMove){

            frameCounterMove = 0;

            boolean validMove = true;

            for (Piece p: current.pieces) {
                //debería existir un id porque para que no se valide que choca con las piezas de la misma
                //figura compara el color de la figura, al ser colores generados a lo random las probabilidades
                //de que choque con una figura del mismo color y se clipee son casi nulas
                if(list[p.y][p.x + dir] != 0 && list[p.y][p.x + dir] != current.color){
                    validMove = false;
                }
            }
            if(validMove){
                for (Piece p: current.pieces) {
                    p.x = p.x + dir;
                }
            }
        }
    }

    public void makeRotation(){

        if(frameCounterRot == frameReactionDelayRot) {

            frameCounterRot = 0;

            Figure copy = new Figure(cols);
            copy.pieces = new ArrayList<>();
            copy.color = current.color;
            for (Piece p : current.pieces) {
                copy.pieces.add(new Piece(p.x, p.y));

            }
            copy.rotate();
            if (isTouching(copy, 0)) {
                return;
            }
            current.rotate();
        }
    }

    private void resetGame(){
        score = 0;
        figures = new ArrayList();
        current = new Figure(cols);
        createBoard();
    }

    private void createBoard() {

        list = new int[rows][cols];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++) {
                if(i == rows - 1 || j == 0 || j == cols - 1){
                    list[i][j] = -1;
                }
                else{
                    list[i][j] = 0;
                }
            }
        }

    }

    private void setFigurePositions(){

        for(Figure f: figures){
            for (Piece p: f.pieces){
                if(isInBoundaries(p.x, p.y) && p.alive) {
                    list[p.y][p.x] = f.color;
                }
            }
        }
    }

}
