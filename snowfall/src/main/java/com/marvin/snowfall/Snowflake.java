package com.marvin.snowfall;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by hmw on 2017/11/30.
 */

public class Snowflake {

    private int parentWidth;
    private int parentHeight;
    private int alphaMin;
    private int alphaMax;
    private int angleMax;
    private int sizeMinInPx;
    private int sizeMaxInPx;
    private int speedMin;
    private int speedMax;
    private Bitmap image;
    private boolean fadingEnabled;
    private boolean alreadyFalling;

    private int size = 0 ;
    private int alpha = 255;
    private Bitmap bitmap = null;
    private double speedX= 0.0;
    private double speedY = 0.0;
    private double positionX = 0.0;
    private double positionY = 0.0;
    private final Randomizer randomizer;

    private Paint paint;

    public Snowflake(int parentWidth, int parentHeight, Bitmap image
            ,int alphaMin,int alphaMax,int angleMax,int sizeMinInPx,int sizeMaxInPx,
                    int speedMin,int speedMax,boolean fadingEnabled,boolean alreadyFalling ){
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.alphaMin = alphaMin;
        this.alphaMax = alphaMax;
        this.angleMax = angleMax;
        this.sizeMinInPx = sizeMinInPx;
        this.sizeMaxInPx = sizeMaxInPx;
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.image = image;
        this.fadingEnabled=fadingEnabled;
        this.alreadyFalling=alreadyFalling;

        randomizer = new Randomizer();
        initPaint();
        reset();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(255,255,255));
        paint.setStyle(Paint.Style.FILL);
    }

    public void reset(double positionY){
        size = randomizer.randomInt(sizeMinInPx, sizeMaxInPx, true);
        if (image!=null){
            if (bitmap==null){
                bitmap = Bitmap.createScaledBitmap(image, size, size, false);
            }
        }
        float speed = (float)(size - sizeMinInPx) / (sizeMaxInPx - sizeMinInPx) * (speedMax - speedMin) + speedMin;

        double angle = Math.toRadians(randomizer.randomDouble(alphaMax) * randomizer.randomSignum());
        if (angle<-1||angle>1){
            angle = 0;
        }
        speedX = speed* Math.sin(angle);
        speedY = speed* Math.cos(angle);
        alpha = randomizer.randomInt(alphaMin, alphaMax, false);
        paint.setAlpha(alpha);

        positionX = randomizer.randomDouble(parentWidth);

        this.positionY = positionY;
    }

    public void reset(){
        size = randomizer.randomInt(sizeMinInPx, sizeMaxInPx, true);
        if (image!=null){
            if (bitmap==null){
                bitmap = Bitmap.createScaledBitmap(image, size, size, false);
            }
        }
        float speed =  (float)(size - sizeMinInPx) / (sizeMaxInPx - sizeMinInPx) * (speedMax - speedMin) + speedMin;
        double angle = Math.toRadians(randomizer.randomDouble(angleMax) * randomizer.randomSignum());
        speedX = speed* Math.sin(angle);
        speedY = speed* Math.cos(angle);

        alpha = randomizer.randomInt(alphaMin, alphaMax, false);
        paint.setAlpha(alpha);

        positionX = randomizer.randomDouble(parentWidth);

        this.positionY=randomizer.randomDouble(parentHeight);
        if (!alreadyFalling){
            this.positionY = this.positionY-parentHeight-size;
        }
    }

    public void update(){
        positionX = positionX+speedX;
        positionY = positionY+speedY;
        if (positionY>parentHeight){
            positionY = -(double)size;
            reset(positionY);
        }
        if (fadingEnabled){
            paint.setAlpha((int) (alpha * ((float) (parentHeight - positionY) / parentHeight)));
        }
    }

    public void draw(Canvas canvas){
        if (bitmap!=null){
            canvas.drawBitmap(bitmap,(float)positionX,(float)positionY,paint);
        }else {
            canvas.drawCircle((float)positionX,(float)positionY,(float)size,paint);
        }
    }
}
