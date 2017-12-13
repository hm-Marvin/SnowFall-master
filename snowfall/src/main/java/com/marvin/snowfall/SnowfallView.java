package com.marvin.snowfall;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by hmw on 2017/11/30.
 */

public class SnowfallView extends SurfaceView implements SurfaceHolder.Callback {

    private int DEFAULT_SNOWFLAKES_NUM = 200;
    private int DEFAULT_SNOWFLAKE_ALPHA_MIN = 150;
    private int DEFAULT_SNOWFLAKE_ALPHA_MAX = 250;
    private int DEFAULT_SNOWFLAKE_ANGLE_MAX = 10;
    private int DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP = 2;
    private int DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP = 8;
    private int DEFAULT_SNOWFLAKE_SPEED_MIN = 2;
    private int DEFAULT_SNOWFLAKE_SPEED_MAX = 8;
    private boolean DEFAULT_SNOWFLAKES_FADING_ENABLED = false;
    private boolean DEFAULT_SNOWFLAKES_ALREADY_FALLING = false;

    private int snowflakesNum;
    private Bitmap snowflakeImage;
    private int snowflakeAlphaMin;
    private int snowflakeAlphaMax;
    private int snowflakeAngleMax;
    private int snowflakeSizeMinInPx;
    private int snowflakeSizeMaxInPx;
    private int snowflakeSpeedMin;
    private int snowflakeSpeedMax;
    private boolean snowflakesFadingEnabled;
    private boolean snowflakesAlreadyFalling;

    //雪花类集合
    private ArrayList<Snowflake> list =new ArrayList<>();

    private SnowfallView.myRunnable myRunnable = new myRunnable();
    private Thread myThread;
    private SurfaceHolder holder;


    public SnowfallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SnowfallView);
        snowflakesNum = typedArray.getInt(R.styleable.SnowfallView_snowflakesNum, DEFAULT_SNOWFLAKES_NUM);
        snowflakeImage = drawable2Bitmap(typedArray.getDrawable(R.styleable.SnowfallView_snowflakeImage));
        snowflakeAlphaMin = typedArray.getInt(R.styleable.SnowfallView_snowflakeAlphaMin, DEFAULT_SNOWFLAKE_ALPHA_MIN);
        snowflakeAlphaMax = typedArray.getInt(R.styleable.SnowfallView_snowflakeAlphaMax, DEFAULT_SNOWFLAKE_ALPHA_MAX);
        snowflakeAngleMax = typedArray.getInt(R.styleable.SnowfallView_snowflakeAngleMax, DEFAULT_SNOWFLAKE_ANGLE_MAX);
        snowflakeSizeMinInPx = typedArray.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMin, dp2Px(DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP));
        snowflakeSizeMaxInPx = typedArray.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMax, dp2Px(DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP));
        snowflakeSpeedMin = typedArray.getInt(R.styleable.SnowfallView_snowflakeSpeedMin, DEFAULT_SNOWFLAKE_SPEED_MIN);
        snowflakeSpeedMax = typedArray.getInt(R.styleable.SnowfallView_snowflakeSpeedMax, DEFAULT_SNOWFLAKE_SPEED_MAX);
        snowflakesFadingEnabled = typedArray.getBoolean(R.styleable.SnowfallView_snowflakesFadingEnabled, DEFAULT_SNOWFLAKES_FADING_ENABLED);
        snowflakesAlreadyFalling = typedArray.getBoolean(R.styleable.SnowfallView_snowflakesAlreadyFalling, DEFAULT_SNOWFLAKES_ALREADY_FALLING);
        typedArray.recycle();

        holder = this.getHolder();
        holder.addCallback(this);

        //设置背景为透明
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取雪花集合
        for (int i=0;i<snowflakesNum;i++){
            list.add(new Snowflake(w, h, snowflakeImage, snowflakeAlphaMin, snowflakeAlphaMax
                    , snowflakeAngleMax, snowflakeSizeMinInPx, snowflakeSizeMaxInPx, snowflakeSpeedMin, snowflakeSpeedMax
                    , snowflakesFadingEnabled, snowflakesAlreadyFalling));
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView==this&&visibility==GONE){
            //初始化雪花类
            try {
                for (Snowflake snowflake :list){
                    snowflake.reset();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()){
            return;
        }
    }

    /**
     * dp转px
     * @param dp
     * @return
     */
    private int dp2Px(int dp){
        return (int) (dp*getResources().getDisplayMetrics().density);
    }

    /**
     * drawble转Bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawable2Bitmap(Drawable drawable){
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (myThread==null){
            myThread = new Thread(myRunnable);
        }
        if(!myThread.isAlive()){
            myThread.start();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (myThread!=null){
            myThread.interrupt();
        }
    }

    public class myRunnable implements Runnable {
        @Override
        public void run() {
            while (true){
                Canvas canvas =null;
                try {
                    synchronized (holder){
                        canvas = holder.lockCanvas();
                        //清除画布
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        for (Snowflake snowflake :list){
                            snowflake.draw(canvas);
                            snowflake.update();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (canvas!=null){
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

            }
        }
    }
}
