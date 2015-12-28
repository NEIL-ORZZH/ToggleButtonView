package com.song.libs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by songhu on 2015/12/21.
 */
public class ToggleButtonView extends View{

    private Paint mPaint;
    private int mBgToggleButtonLineColor;
    private float mBgToggleButtonLineHeight;
    private int mCircleToggleButtonColor;
    private int  mBgToggleButtonLineRadius;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private int mCurrentLeft;
    private int mCircleToggleButtonPadding;
    private int mCircleToggleButtonBottom;
    private boolean mState;
    private PorterDuffXfermode mPorterDuffXfermode;
    private float mOffset;
    private VelocityTracker mVelocityTracker;
    private float mScaledMaximumFlingVelocity;
    private int direction;
    private boolean mAnimatorRunning;
    private int onColor;
    private int offColor;
    private boolean mPreviousState;

    private OnToggleButtonChanged onToggleButtonChanged;

    private GestureDetectorCompat mGestureDetectorCompat;

    public ToggleButtonView(Context context) {
        this(context, null);
    }

    public ToggleButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public interface OnToggleButtonChanged{
        void onToggle(boolean toggle);
    }

    /**
     * init view
     */
    private void init(Context mContext, AttributeSet attributeSet) {

        TypedArray ta = mContext.obtainStyledAttributes(attributeSet, R.styleable.ToggleButtonView);
        mBgToggleButtonLineColor = ta.getColor(R.styleable.ToggleButtonView_bgLineColor, Color.parseColor("#0D4300"));
        mCircleToggleButtonColor = ta.getColor(R.styleable.ToggleButtonView_circleColor, Color.parseColor("#177800"));
        onColor = ta.getColor(R.styleable.ToggleButtonView_onColor, Color.parseColor("#0D4300"));
        offColor = ta.getColor(R.styleable.ToggleButtonView_offColor, Color.WHITE);
        mBgToggleButtonLineRadius = (int) ta.getDimension(R.styleable.ToggleButtonView_bgLineColorRadius, 10f);
        mCircleToggleButtonPadding = (int) ta.getDimension(R.styleable.ToggleButtonView_circlePadding, 5f);
        mBgToggleButtonLineHeight = (int)ta.getDimension(R.styleable.ToggleButtonView_bgLineHeight, 10f);
        mOffset = ta.getFloat(R.styleable.ToggleButtonView_offset, 0.5f);
        ta.recycle();

        mVelocityTracker = VelocityTracker.obtain();
        mScaledMaximumFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mRectF = new RectF();
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        mGestureDetectorCompat = new GestureDetectorCompat(getContext(), new ToggleSimpleOnGestureListener());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        initBgPaint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    class ToggleSimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mCurrentLeft-=distanceX;
            mCurrentLeft = Math.min(mCircleToggleButtonBottom, Math.max(mCurrentLeft, 0));
            postInvalidate();
            return false;
        }

    }

    class SimpleAnimatorListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBgToggleButtonLine(canvas);
        drawCircleToggleButton(canvas);
        super.onDraw(canvas);
    }

    /**
     * draw ToggleButton's Circle
     * @param canvas
     */
    private void drawCircleToggleButton(Canvas canvas) {
        resetCirclePaint();
        int mCircleRadius = mHeight/2 - mCircleToggleButtonPadding;
        mRectF.set(mCurrentLeft, mCircleToggleButtonPadding, mCurrentLeft + 2 * mCircleRadius, mHeight - mCircleToggleButtonPadding);
        canvas.drawRoundRect(mRectF, mCircleRadius, mCircleRadius, mPaint);
        canvas.save();
    }

    /**
     * init ToggleButton's background Paint
     */
    private void initBgPaint(){
        mPaint.setColor(mBgToggleButtonLineColor);
        mPaint.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.INNER));
        mPaint.setXfermode(null);
    }

    /**
     * reset ToggleButton's circle Paint
     */
    private void resetCirclePaint() {
        mPaint.setColor(mCircleToggleButtonColor);
        mPaint.setXfermode(mPorterDuffXfermode);
        mPaint.setMaskFilter(null);
    }

    /**
     * draw ToggleButton's background
     * @param canvas
     */
    private void drawBgToggleButtonLine(Canvas canvas) {
        initBgPaint();
        float bgTop = mHeight/2 - mBgToggleButtonLineHeight/2;
        canvas.drawRoundRect(new RectF(0, bgTop, mWidth, bgTop + mBgToggleButtonLineHeight), mBgToggleButtonLineRadius, mBgToggleButtonLineRadius, mPaint);
        canvas.save();
    }

    /**
     * control event
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN && mAnimatorRunning)
            return false;
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
            doActionMove();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            onActionUp();
            return  super.onTouchEvent(event);
        }
        return mGestureDetectorCompat.onTouchEvent(event);
    }

    /**
     * do when action move
     */
    private void doActionMove() {
        final float xVelocity = mVelocityTracker.getXVelocity();
        if(xVelocity > mVelocityTracker.getYVelocity() && Math.abs(xVelocity) > 1000){
            direction = xVelocity>0?1:0;
        }
        invalidateToggleColor();
    }

    private void invalidateToggleColor(){
        if(mCurrentLeft == mCircleToggleButtonBottom){
            mState = true;
            setToggleColor(mState);
            invalidate();
        } else if(mCurrentLeft == 0){
            mState = false;
            setToggleColor(mState);
            invalidate();
        }
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if(null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * do when action up
     */
    private void onActionUp() {
        clearVelocityTracker();
        invalidateToggleColor();
        if(obtainState())
            open();
        else
            close();
        direction = 0;
    }

    /**
     * recycle VelocityTracker
     */
    private void clearVelocityTracker() {
        if(null != mVelocityTracker){
            try {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @return switch state
     */
    private boolean obtainState(){
        if(direction != 0){
            mState = direction >0?true:false;
        }else
            mState = mCurrentLeft > mCircleToggleButtonBottom * mOffset?true:false;
        return mState;
    }

    /**
     * set toggleButton state
     * @param state
     */
    public void setToggleButton(boolean state){
        mState = state;
        mPreviousState = state;
        invalidate();
    }



    public void setOnToggleButtonChangedListener(OnToggleButtonChanged onToggleButtonChanged){
        this.onToggleButtonChanged = onToggleButtonChanged;
    }

    private void onToggle(boolean toggle){
        if(null != onToggleButtonChanged){
            onToggleButtonChanged.onToggle(toggle);
        }
    }

    private void open(){
        if(mPreviousState != mState){
            onToggle(true);
            mPreviousState = true;
        }
        if(mAnimatorRunning)return;
        mState = true;
        startAnimator(mCurrentLeft, mCircleToggleButtonBottom);
    }

    private void close(){
        if(mPreviousState != mState){
            onToggle(false);
            mPreviousState = false;
        }
        if(mAnimatorRunning)return;
        mState = false;
        startAnimator(mCurrentLeft, 0);
    }


    private void setToggleColor(boolean state){
        if(!state)
            mBgToggleButtonLineColor = offColor;
        else
            mBgToggleButtonLineColor = onColor;
    }

    /**
     * offer animator
     * @param from
     * @param to
     */
    private void startAnimator(final int from, final int to){
        if(to == 0){
            setToggleColor(false);
        } else{
            setToggleColor(true);
        }
        int duration = to==0?300:300 * (1 - mCurrentLeft / mCircleToggleButtonBottom);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new SimpleAnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorRunning = false;
                invalidate();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mAnimatorRunning = true;
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mCurrentLeft = value;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * measure widget
     * @param widthMeasureSpec The measured width of this view
     * @param heightMeasureSpec The measured height of this view
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCircleToggleButtonBottom  = mWidth - 2*(mHeight/2 - mCircleToggleButtonPadding);
        setMeasuredDimension(measureSize((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()), widthMeasureSpec),
                measureSize((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics()), heightMeasureSpec));
        if(mState)
            mCurrentLeft = mCircleToggleButtonBottom;
        else
            mCurrentLeft = 0;
    }

    private int measureSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * saved state
     * @return Parcelable
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.state = mState?1:0;
        savedState.mCircleToggleButtonBottom = mCircleToggleButtonBottom;
        return savedState;
    }

    /**
     * restore widget state
     * @param state Parcelable
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        try {
            super.onRestoreInstanceState(state);
        }catch (Exception e){
            e.printStackTrace();
        }
        mCircleToggleButtonBottom = savedState.mCircleToggleButtonBottom;
        if(savedState.state == 0)
            close();
        else
            open();
    }

    private static class SavedState extends BaseSavedState {
            int state;
            int mCircleToggleButtonBottom;

            SavedState(Parcelable superState) {
                super(superState);
            }

            private SavedState(Parcel in) {
                super(in);
                state = in.readInt();
                mCircleToggleButtonBottom = in.readInt();
            }

            @Override
            public void writeToParcel(Parcel out, int flags) {
                super.writeToParcel(out, flags);
                out.writeInt(state);
                out.writeInt(mCircleToggleButtonBottom);
            }

            public static final Creator<SavedState> CREATOR
                    = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }

}
