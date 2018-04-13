package com.example.nguyenthanhxuan.gamebancocaro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Thanh Xuan on 3/21/2018.
 */

public class ChessBoard {

    private Context context;
    private Canvas canvas;
    private Paint paint;
    private boolean isEnable;

    private int bitmap_width;//chieu dai
    private int bitmap_height;//chieu rong
    private int col_no;//cot
    private int row_no;//dong

    private List<Line> linenList;
    private int [][] board;//ban co

    private int player;

    int centerrec =50;

    public ChessBoard(Context context, Canvas canvas, Paint paint, int bitmap_width, int bitmap_height, int col_no, int row_no) {
        this.context = context;
        this.canvas = canvas;
        this.paint = paint;
        this.bitmap_width = bitmap_width;
        this.bitmap_height = bitmap_height;
        this.col_no = col_no;
        this.row_no = row_no;
    }

    //khởi tạo
    public void init (){
        linenList = new ArrayList<Line>();
        isEnable=true;
        for (int i = 0; i <=row_no ; i++) {
             linenList.add(new Line(new Point(0,i*(bitmap_height/row_no)), new Point(bitmap_width, i*(bitmap_height/row_no))));
        }

        for (int i = 0; i <=col_no ; i++) {
            linenList.add(new Line(new Point(i*(bitmap_width/col_no),0), new Point(i*(bitmap_width/col_no)  ,bitmap_height)));
        }
        Log.d("size",linenList.size() +"");
        player=0;
        board = new int[row_no][col_no];
        for (int i = 0; i <row_no ; i++) {
            for (int j = 0; j <col_no ; j++) {
                board[i][j]=-1;//chưa có người đi
            }
        }
    }

    //vẽ bàn cờ

    public void drawcheckboard(){
        Line line;
        for (int i = 0; i <linenList.size() ; i++) {
            line = linenList.get(i);
            canvas.drawLine(line.getStartP().getX(),line.getStartP().getY(), line.getEndP().getX(), line.getEndP().getY(), paint);
        }
    }

    public boolean onTouch (View view, MotionEvent event){
        if (isEnable==false) return false;
        if( event.getAction() == MotionEvent.ACTION_DOWN){
            int x= (int) event.getX();
            int y= (int)event.getY();

            int r= view.getWidth();//rộng lưới
            int h = view.getHeight();//cao lưới

            int r1 = r/col_no;//rộng 1 ô
            int h1 = h/row_no;//cao 1 ô

            Log.d("rong", x/r1 +" - "+ y/h1);

            if (board[y/h1][x/r1]== -1){        //vị trí ô vừa đánh nếu =-1
                board[y/h1][x/r1]= player;

                Point center = new Point((int) ((x/r1 +0.5 )*100), (int) ((y/h1+0.5)*100));

                Point leftTop = new Point(center.getX() - centerrec/2, center.getY() - centerrec/2);
                Point rightBottom = new Point(center.getX()+ centerrec/2, center.getY()+centerrec/2);

                Bitmap subBitmap=null;
                if (player==0) {
                    subBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cross);
                }
                if (player==1) {
                    subBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tick);
                }
                if (subBitmap!=null)
                    canvas.drawBitmap(subBitmap,new Rect(0,0, subBitmap.getWidth(),subBitmap.getHeight()),
                            new Rect(leftTop.getX(),leftTop.getY(),rightBottom.getX(), rightBottom.getY()),paint);

                view.invalidate();
            }
            if(checkHang()==1||checkCot()==1||checkCheoPhai()==1||checkCheoTrai()==1) {
                    Toast.makeText(context,"Người chơi "+player+" đã chiến thắng",Toast.LENGTH_SHORT).show();
                isEnable=false;
            }
            player = (player+1)%2;
        }
        return true;
    }

    public int checkHang(){
        int win=0,hang=0,n=0,k=0;
        boolean check=false;
        for (int i=0;i<row_no;i++){
            for (int j=0;j<col_no;j++){
                if (check){
                    if (board[i][j]==player){
                        hang++;
                        if (hang>4){
                            win=1;
                            break;
                        }
                        continue;
                    }else {
                        check=false;
                        hang=0;
                    }
                }
                if (board[i][j]==player){
                    check=true;
                    hang++;
                }else{
                    check = false ;
                }
            }
            hang=0;
        }
        return win;
    }
    public int checkCot(){
        int win=0,cot=0;
        boolean check=false;
        for (int j=0;j<col_no;j++){
            for (int i=0;i<row_no;i++){
                if (check){
                    if (board[i][j]==player){
                        cot++;
                        if (cot>4){
                            win=1;
                            break;
                        }
                        continue;
                    }else {
                        check=false;
                        cot=0;
                    }
                }
                if (board[i][j]==player){
                    check=true;
                    cot++;
                }else{
                    check = false ;
                }
            }
            cot=0;
        }
        return win;
    }
    public int checkCheoPhai(){
        int win=0,cheop=0,n=0,k=0;
        boolean check=false;
        for (int i=row_no-1;i>=0;i--){
            for (int j=0;j<col_no;j++){
                if (check){
                    if (board[n-j][j]==player){
                        cheop++;
                        if(cheop>4){
                            win=1;
                            break;
                        }
                        continue;
                    }else {
                        check=false;
                        cheop=0;
                    }
                }
                if (board[i][j]==player){
                    n=i+j;
                    check=true;
                    cheop++;
                }else{
                    check = false ;
                }
            }
            cheop =0;
            check = false ;
        }
        return win;
    }
    public int checkCheoTrai(){
        int win=0,cheot=0,n=0;
        boolean check=false;
        for (int i=0;i<row_no;i++){
            for (int j=col_no-1;j>=0;j--){
                if (check){
                    if (board[n-j-2*cheot][j]==player){
                        cheot++;
                        System.out.print("+"+j);
                        if (cheot>4){
                            win=1;
                            break;
                        }
                        continue;
                    }else {
                        check=false;
                        cheot=0;
                    }
                }
                if (board[i][j]==player){
                    n=i+j;
                    check=true;
                    cheot++;
                }else{
                    check = false ;
                }
            }
            n=0;
            cheot = 0 ;
            check = false ;
        }
        return win;
    }
}

