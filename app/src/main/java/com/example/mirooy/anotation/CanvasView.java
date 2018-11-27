package com.example.mirooy.anotation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.widget.SeekBar;

/**
 * Created by mirooy on 2018. 2. 10..
 */

public class CanvasView extends View{
    public int width;
    public int height;
    private Bitmap bit;
    private Canvas canvas;
    private Path path ;
    private Paint newPaint;
    private float X, Y;
    private Paint paint;
    private static final float TOLERANCE = 5;
    Context context;
    private static float paintWidth;
    private boolean erase = false;
    private int paintColor = Color.BLACK;


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(paintColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        newPaint = new Paint();
        update();
    }
    public void setErase(boolean isErase) {
        erase = isErase;
        if (erase) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            paint.setXfermode(null);
        }
    }



    private void onStartTouch(float x, float y) {
        path.moveTo(x,y);
        X = x;
        Y = y;
    }
    private void moveTouch(float x, float y ) {
        float mx = Math.abs(x - X);
        float my = Math.abs(y - Y);
        if (mx <= TOLERANCE || my >= TOLERANCE) {
            path.quadTo(X,Y,(x + X) / 2 ,(y + Y)/ 2);
            X = x ;
            Y = y;
        }
    }
    public void clearCanvas() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
    private void upTouch() {
        path.lineTo(X,Y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                onStartTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                canvas.drawPath(path,paint);
                path.reset();
                invalidate();
                break;

        }

        invalidate();
        return true;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        bit = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bit);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        newPaint = new Paint();
        paint.setColor(paintColor);
        canvas.drawPath(path, paint);
        canvas.drawBitmap(bit,0,0,newPaint);
        }


    public void setStrokeWidth(float newSize) {
        paintWidth = newSize;
        update();
    }
    private void update() {
        paint.setStrokeWidth(paintWidth);

    }

    public void setColor(int color){
        setErase(false);
        paintColor = color;
    }
}
