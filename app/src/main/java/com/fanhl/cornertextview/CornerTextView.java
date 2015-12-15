package com.fanhl.cornertextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 显示在父视图右上角的斜TextView
 */
public class CornerTextView extends TextView {
    private static final float ANGEL_RIGHT_TOP                 = 45f;
    private static final float ANGEL_LEFT_TOP                  = -45f;
    private static final float DISTANCE_DEFAULT                = 120f;
    private static final float STROKE_WIDTH_DEFAULT            = 0f;
    private static final int   CORNER_BACKGROUND_COLOR_DEFAULT = 0xFF00B94D;
    //根号2
    private static final float REAGAN_TWO                      = (float) Math.sqrt(2);

    /**
     * 显示在右上角还是左上角
     */
    private Gravity gravity;
    /**
     * 有边角的距离
     */
    private float distance = DISTANCE_DEFAULT;
    /**
     * 文字与色带的边距
     */
    private float strokeWidth;
    /**
     * 色带颜色
     */
    private int   beltColor;

    // 文字底部到边角顶点的距离
    private float disToBottom;
    Paint mBgPaint;
    private Path  path;
    // 背景色带的高度*根号2
    private float beltHeight;

    public CornerTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public CornerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CornerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setGravity(android.view.Gravity.CENTER);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CornerTextView, defStyle, 0);

        int gravityInt = a.getInt(R.styleable.CornerTextView_gravity, 0);
        if (gravityInt == 0) {
            gravity = Gravity.RIGHT_TOP;
        } else {
            gravity = Gravity.LEFT_TOP;
        }
        distance = a.getDimension(R.styleable.CornerTextView_distance, DISTANCE_DEFAULT);
        strokeWidth = a.getDimension(R.styleable.CornerTextView_strokeWidth, STROKE_WIDTH_DEFAULT);
        beltColor = a.getColor(R.styleable.CornerTextView_beltColor, CORNER_BACKGROUND_COLOR_DEFAULT);

        a.recycle();

        //绘制斜条用
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(beltColor);
        mBgPaint.setStyle(Paint.Style.FILL);

        path = new Path();

        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        Paint.FontMetrics fm = getPaint().getFontMetrics();

        float mTextHeight = (float) Math.ceil(fm.bottom - fm.top);

        disToBottom = (float) (1.5 * mTextHeight + distance + strokeWidth);
        beltHeight = (mTextHeight + 2 * strokeWidth) * REAGAN_TWO;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width  = (int) (disToBottom * REAGAN_TWO);
        int height = (int) (disToBottom * REAGAN_TWO);

        path.reset();
        if (gravity == Gravity.RIGHT_TOP) {
            path.moveTo(0, 0);
            path.lineTo(beltHeight * REAGAN_TWO, 0);
            path.lineTo(width, height - beltHeight * REAGAN_TWO);
            path.lineTo(width, height);
        } else {
            path.moveTo(0, height);
            path.lineTo(0, height - beltHeight * REAGAN_TWO);
            path.lineTo(width - beltHeight * REAGAN_TWO, 0);
            path.lineTo(width, 0);
        }
        path.close();

        setMeasuredDimension(width & MEASURED_SIZE_MASK, height & MEASURED_SIZE_MASK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, mBgPaint);

        canvas.save();
        if (gravity == Gravity.RIGHT_TOP) {
            canvas.translate(beltHeight / REAGAN_TWO / 2, -beltHeight / REAGAN_TWO / 2);
            canvas.rotate(ANGEL_RIGHT_TOP, this.getWidth() / 2f, this.getHeight() / 2f);
        } else {
            canvas.translate(-beltHeight / REAGAN_TWO / 2, -beltHeight / REAGAN_TWO / 2);
            canvas.rotate(ANGEL_LEFT_TOP, this.getWidth() / 2f, this.getHeight() / 2f);
        }

        super.onDraw(canvas);

        canvas.restore();
    }

    public enum Gravity {
        LEFT_TOP, RIGHT_TOP
    }
}
