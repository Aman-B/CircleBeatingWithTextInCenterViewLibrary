package com.bewtechnologies.CircleBeatingWithTextInCenterView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class CircleBeatingWithTextInCenterView extends View {


    private RectF mArcRect;
    private Rect mTextRect= new Rect();
    private Paint mArcPaint,mProgressPaint;
    private int mArcRadius;

    int widthForRect,heightForRect;
    private int top,left;

    boolean isAnimationRunning =false;
    private ObjectAnimator scaleUpAndDown;
    private Paint mTextPaint;
    private int arcDiameter;
    private boolean isShowText=false;
    private float strokeWidthForArc=2;
    private int colorForArc=Color.BLUE;
    private String textForDrawText="some time ";
    private int paddingLeft;
    private float textSize=54;
    private int colorForText=Color.RED;
    private long timeForOneBeat = 500;

    Canvas mCanvas;
    private int xPos,yPos;

    int isStartOrStop=1;  //0 for stop, 1 for start.

    public CircleBeatingWithTextInCenterView(Context context) {
        super(context);
    }

    public CircleBeatingWithTextInCenterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context,attrs);



    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        final int min = Math.min(w, h);

        paddingLeft = getPaddingLeft();

        arcDiameter = min-paddingLeft;

        mArcRadius = arcDiameter / 2;
        top = h / 2 - (arcDiameter / 2);
        left = w / 2 - (arcDiameter / 2);


        widthForRect=w/4+arcDiameter/2;
        heightForRect=h/4+arcDiameter/2;



        mArcRect.set(left+strokeWidthForArc/2.0f, top+strokeWidthForArc/2.0f, left + arcDiameter-strokeWidthForArc/2.0f, top + arcDiameter-strokeWidthForArc/2.0f);



    }


    public void init(Context context, AttributeSet attrs)
    {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleBeatingWithTextInCenterView,
                0, 0);

        try {
            isShowText = a.getBoolean(R.styleable.CircleBeatingWithTextInCenterView_showText, false);
            strokeWidthForArc = a.getFloat(R.styleable.CircleBeatingWithTextInCenterView_strokeWidthForArc, 2);
            textSize = a.getFloat(R.styleable.CircleBeatingWithTextInCenterView_textSize, 54);
            colorForArc=  a.getColor(R.styleable.CircleBeatingWithTextInCenterView_circleColor,Color.BLUE);
            colorForText=  a.getColor(R.styleable.CircleBeatingWithTextInCenterView_textColor,Color.BLUE);
        } finally {
            a.recycle();
        }


        mArcRect= new RectF();

        mArcPaint = new Paint();
        mArcPaint.setColor(colorForArc);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(strokeWidthForArc);

        mTextPaint = new Paint();
        mTextPaint.setColor(colorForText);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(textSize);



    }

    private void startOrStopAnimation() {

        Log.i("isAnimation startostop ", "started isStartOrStop "+isStartOrStop);

        if(isStartOrStop==1)
        {
            isAnimationRunning=true;
            scaleUpAndDown = ObjectAnimator.ofPropertyValuesHolder(
                    this,
                    PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.2f));
            scaleUpAndDown.setDuration(timeForOneBeat);
            scaleUpAndDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    Log.i("isAnimation repeating ", "started isStartOrStop "+isStartOrStop);
                    if(isStartOrStop==0)
                    {
                        animator.cancel();
                    }

                }
            });
            scaleUpAndDown.setRepeatCount(ObjectAnimator.INFINITE);
            scaleUpAndDown.setRepeatMode(ObjectAnimator.REVERSE);



            scaleUpAndDown.start();
        }
        else
        {
            if(scaleUpAndDown!=null && scaleUpAndDown.isRunning())
            {
                scaleUpAndDown.cancel();
            }
        }




    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvas=canvas;
        mTextPaint.getTextBounds(textForDrawText, 0, textForDrawText.length(), mTextRect);

        xPos =(int) (mArcRect.centerX() - mTextRect.width() / 2);
        yPos =(int)( mArcRect.centerY() - (mTextPaint.ascent()+mTextPaint.descent())/2);

        // draw the arc
        canvas.drawArc(mArcRect, -90, 360, false, mArcPaint);

        if(isShowText)
        {
            canvas.drawText(textForDrawText,xPos,yPos,mTextPaint);
        }

        Log.i("isAnimation ondraw ", "started isStartOrStop "+isStartOrStop);

        //uncomment below
        startOrStopAnimation();



    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        try {

            switch (visibility)
            {
                case VISIBLE:
                    if(!isAnimationRunning)
                        scaleUpAndDown.cancel();
                        startOrStopAnimation();
                    break;
                case INVISIBLE:
                    if(isAnimationRunning)
                        scaleUpAndDown.cancel();
                    break;


            }


        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void stopAnimation(){
        Log.i("isAnimation sdfd ", " "+scaleUpAndDown.isRunning());
//        if(scaleUpAndDown.isRunning())
        {
            //clearAnimation();

            scaleUpAndDown.cancel();
            Log.i("isAnimation sdfd 2 ", " "+scaleUpAndDown.isRunning());

            isStartOrStop=0;



        }
    }
    public synchronized void setText(String text) {
        Log.i("isAnimation settext ", "jhjh");

        textForDrawText = text;
        invalidate();
        requestLayout();

    }
}
