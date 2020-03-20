package com.example.colortiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.util.Random;

public class RectangleView extends View {
    class Rectangle {
        int x, y, width;
        boolean isDark;

        Rectangle(int x, int y, int width, boolean isDark) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.isDark = isDark;
        }

        boolean wasClicked(float x, float y) {
            if ((x > this.x) && (x < this.x + this.width) &&
                    (y > this.y) && (y < this.y + this.width)) {
                return true;
            }
            return false;
        }

        void convertColor() {
            this.isDark = !this.isDark;
        }
    }

    Paint p = new Paint();
    Rectangle[][] rectangles = new Rectangle[4][4];


    public RectangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleView(Context context) {
        super(context);
    }

    public void initializeRectangles(int width) {
        int rect_width = (width - 100) / 4;

        Random random = new Random();
        int x = 10;
        int y = 20;
        for (int i = 0; i < rectangles.length; i ++) {
            for (int j = 0; j < rectangles[0].length; j ++) {

                int color = random.nextInt(2);
                boolean isDark = color == 1 ? true : false;

                Rectangle rectangle = new Rectangle(x, y, rect_width, isDark);
                rectangles[i][j] = rectangle;

                x += rect_width + 20;
            }
            x = 10;
            y += rect_width + 20;
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initializeRectangles(getWidth());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        outerloop:
        for (int i = 0; i < rectangles.length; i ++) {
            for (int j = 0; j < rectangles[0].length; j++) {
                if (rectangles[i][j].wasClicked(event.getX(), event.getY())){
                    convertRowAndColumn(i, j);
                    break outerloop;
                }
            }
        }
        invalidate();
        checkWin();
        return false;
    }

    public void convertRowAndColumn(int row, int column) {

        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i][column].convertColor();
        }
        for (int j = 0; j < rectangles[0].length; j++) {
            if (j != column) {
                rectangles[row][j].convertColor();
            }
        }
    }

    public void checkWin() {
        boolean win = true;
        boolean prevColor = rectangles[0][0].isDark;

        outer:
        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[0].length; j++) {
                if (prevColor != rectangles[i][j].isDark) {
                    win = false;
                    break outer;
                }
            }
        }

        if (win) {
            Toast toast = Toast.makeText(this.getContext(),
                    "Вы выиграли", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        Integer darkColor = Color.rgb(0, 0, 102);
        Integer lightColor = Color.rgb(255, 51, 119);

        for (int i = 0; i < rectangles.length; i ++) {
            for (int j = 0; j < rectangles[0].length; j++) {
                if (rectangles[i][j].isDark) {
                    p.setColor(darkColor);
                } else {
                    p.setColor(lightColor);
                }
                canvas.drawRect(rectangles[i][j].x, rectangles[i][j].y,
                        rectangles[i][j].x + rectangles[i][j].width,
                        rectangles[i][j].y + rectangles[i][j].width, p);
            }
        }
    }
}
