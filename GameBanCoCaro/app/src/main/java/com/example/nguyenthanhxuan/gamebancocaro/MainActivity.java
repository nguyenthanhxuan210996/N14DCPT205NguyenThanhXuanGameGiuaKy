package com.example.nguyenthanhxuan.gamebancocaro;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    public static final int ROW_NO=8;// 8 hàng
    public static final int COL_NO=8;// 8 cột
    public static final int BITMAP_WIDTH=800; //dài 800
    public static final int BITMAP_HEIGHT=800; //
    ChessBoard chessBoard;
    ImageView imageView;
    TCPClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        bitmap = Bitmap.createBitmap(BITMAP_WIDTH,BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setARGB(255,255,0,0);
        paint.setStrokeWidth(2);
        chessBoard = new ChessBoard(MainActivity.this, canvas, paint,BITMAP_WIDTH,BITMAP_HEIGHT, COL_NO,ROW_NO);
        chessBoard.init();
        chessBoard.drawcheckboard();
        imageView.setImageBitmap(bitmap);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return chessBoard.onTouch(v,event);
            }
        });

    }
    //xử lý đa tiến trình
    public class connectTask extends AsyncTask<Void,Void,Void>{
        // tham số thứ 1: khi gọi thực thi tiến trình truyền vào doInBackground
        // tham số thứ 2: Update giao diện khi thực thi truyền vào onProgreessUpdate
        // tham số thứ 3: lưu trữ kết quả khi tiến trình thực hiện xong
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                client=new TCPClient("192.168.43.204",1234);
                Scanner scan=new Scanner(System.in);
                client.run();
                client.start();
                client.send("abc");
            }catch (Exception e){

            }
            return null;
        }
    }
}

