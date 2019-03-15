package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayer.create(this, R.raw.tetris_song).start();



        showMenu("Tetris", "comenzar", this);
    }



    public boolean onTouchEvent(MotionEvent e){
        board.moveCurrentManager(e.getX(), e.getY());
        return true;
    }

    public void showMenu(String title, String btntext, final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View menu = getLayoutInflater().inflate(R.layout.menu, null);

        Button btnMenu = menu.findViewById(R.id.btnMenu);
        TextView txtTitle = menu.findViewById(R.id.txtMenuTitle);

        txtTitle.animate().alpha(255).setDuration(1000).start();

        txtTitle.setText(title);
        btnMenu.setText(btntext);

        builder.setView(menu);
        final AlertDialog dialog = builder.create();

        dialog.show();

        btnMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                board = new Board(context);

                setContentView(board);
                dialog.hide();
            }
        });

    }
}
