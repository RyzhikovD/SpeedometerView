package ru.sberbankmobile.speedometer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SpeedometerView extends View {

    private final int MAX_POINTER_ANGLE = 270;
    private final float SCALE_STROKE_WIDTH = 64;
    private final int START_ANGLE = 135;

    private int mLowSpeedColor;
    private int mNormalSpeedColor;
    private int mHighSpeedColor;
    private int mPointerColor;

    private int mTextSize;

    private int mMaxSpeed;
    private int mCurrentSpeed;

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF mSpeedometerRect = new RectF(0, 0, 700, 700);
    private Rect mTextBounds = new Rect();

    public SpeedometerView(Context context) {
        super(context);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpeedometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(SCALE_STROKE_WIDTH / 2, SCALE_STROKE_WIDTH / 2);
        canvas.drawArc(mSpeedometerRect, START_ANGLE, MAX_POINTER_ANGLE, false, mCirclePaint);

        drawCurrentSpeed(canvas);
        drawMaxSpeed(canvas);
        drawPointer(canvas);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        extractAttributes(context, attrs);
        configureCirclePaint();
        configurePointerPaint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(getColorForCurrentSpeed());
    }

    private void configurePointerPaint() {
        mPointerPaint.setStrokeWidth(SCALE_STROKE_WIDTH / 2);
        mPointerPaint.setColor(mPointerColor);
    }

    private void configureCirclePaint() {
        mCirclePaint.setStrokeWidth(SCALE_STROKE_WIDTH);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.RED);
    }

    private void drawCurrentSpeed(Canvas canvas) {
        final String currentSpeed = String.valueOf(mCurrentSpeed);
        mTextPaint.getTextBounds(currentSpeed, 0, currentSpeed.length(), mTextBounds);
        mTextPaint.setColor(getColorForCurrentSpeed());
        float x = mSpeedometerRect.width() / 2f - mTextBounds.width() / 2f - mTextBounds.left;
        float y = (mSpeedometerRect.height() / 2f + mTextBounds.height() / 2f - mTextBounds.bottom) * 3 / 2;
        canvas.drawText(currentSpeed, x, y, mTextPaint);
    }

    private void drawMaxSpeed(Canvas canvas) {
        int currentColor = mTextPaint.getColor();
        mTextPaint.setColor(Color.GRAY);
        canvas.drawText(String.valueOf(mMaxSpeed), mSpeedometerRect.width() - mTextBounds.width(), mSpeedometerRect.height(), mTextPaint);
        mTextPaint.setColor(currentColor);
    }

    private void drawPointer(Canvas canvas) {
        canvas.save();
        canvas.rotate(START_ANGLE + mCurrentSpeed * MAX_POINTER_ANGLE / mMaxSpeed, mSpeedometerRect.centerX(), mSpeedometerRect.centerY());
        mPointerPaint.setColor(mPointerColor);
        canvas.drawLine(mSpeedometerRect.centerX(), mSpeedometerRect.centerY(), mSpeedometerRect.height(), mSpeedometerRect.centerY(), mPointerPaint);
        canvas.restore();
    }

    private void extractAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        final Resources.Theme theme = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.SpeedometerView, R.attr.speedometerStyle, 0);
        try {
            mLowSpeedColor = typedArray.getColor(R.styleable.SpeedometerView_lowSpeedColor, Color.BLUE);
            mNormalSpeedColor = typedArray.getColor(R.styleable.SpeedometerView_normalSpeedColor, Color.BLACK);
            mHighSpeedColor = typedArray.getColor(R.styleable.SpeedometerView_highSpeedColor, Color.RED);
            mPointerColor = typedArray.getColor(R.styleable.SpeedometerView_pointerColor, Color.BLACK);
            mMaxSpeed = typedArray.getInteger(R.styleable.SpeedometerView_maxSpeed, 260);
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.SpeedometerView_textSize, getResources().getDimensionPixelSize(R.dimen.defaultTextSize));
        } finally {
            typedArray.recycle();
        }
    }

    private int getColorForCurrentSpeed() {
        if (mCurrentSpeed < 50) {
            return mLowSpeedColor;
        } else if (mCurrentSpeed > 100) {
            return mHighSpeedColor;
        } else {
            return mNormalSpeedColor;
        }
    }

    public void setCurrentSpeed(int speed) {
        if ((speed > mMaxSpeed) || (speed < 0)) {
            throw new IllegalArgumentException(getResources().getString(R.string.illegal_speed_argument) + mCurrentSpeed);
        }
        mCurrentSpeed = speed;
    }

    public int getCurrentSpeed() {
        return mCurrentSpeed;
    }

    public int getNormalSpeedColor() {
        return mNormalSpeedColor;
    }

    public void setNormalSpeedColorColor(int color) {
        mNormalSpeedColor = color;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getMaxSpeed() {
        return mMaxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        mMaxSpeed = maxSpeed;
    }

    public int getLowSpeedColor() {
        return mLowSpeedColor;
    }

    public void setLowSpeedColor(int lowSpeedColor) {
        mLowSpeedColor = lowSpeedColor;
    }

    public int getHighSpeedColor() {
        return mHighSpeedColor;
    }

    public void setHighSpeedColor(int highSpeedColor) {
        mHighSpeedColor = highSpeedColor;
    }

    public int getPointerColor() {
        return mPointerColor;
    }

    public void setPointerColor(int pointerColor) {
        mPointerColor = pointerColor;
    }
}
