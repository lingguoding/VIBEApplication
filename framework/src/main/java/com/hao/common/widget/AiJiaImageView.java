package com.hao.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hao.common.R;


/**
 * @Package com.daoda.aijiacommunity.common.widget
 * @作用：圆形、正方形imageView
 * @作者：linguoding
 * @日期:2016/3/18 10:13
 */
public class AiJiaImageView extends ImageView {
    private int mCornerRadius = 0;
    private boolean mIsCircle = false;
    private boolean mIsSquare = false;
    private int mBorderWidth = 0;
    private int mBorderColor = Color.WHITE;

    private Paint mBorderPaint;
    private OnDrawableChangedCallback mOnDrawableChangedCallback;

    public AiJiaImageView(Context context) {
        this(context, null);
    }

    public AiJiaImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AiJiaImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initCustomAttrs(context, attrs);

        initBorderPaint();
    }

    private void initBorderPaint() {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AiJiaImageView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.AiJiaImageView_iv_isCircle) {
            mIsCircle = typedArray.getBoolean(attr, mIsCircle);
        } else if (attr == R.styleable.AiJiaImageView_iv_cornerRadius) {
            mCornerRadius = typedArray.getDimensionPixelSize(attr, mCornerRadius);
        } else if (attr == R.styleable.AiJiaImageView_iv_isSquare) {
            mIsSquare = typedArray.getBoolean(attr, mIsSquare);
        } else if (attr == R.styleable.AiJiaImageView_iv_borderWidth) {
            mBorderWidth = typedArray.getDimensionPixelSize(attr, mBorderWidth);
        } else if (attr == R.styleable.AiJiaImageView_iv_borderColor) {
            mBorderColor = typedArray.getColor(attr, mBorderColor);
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        if (resId != 0 && mCornerRadius > 0) {
            setImageDrawable(getRoundedDrawable(getContext(), resId, mCornerRadius));
        } else if (resId != 0 && mIsCircle) {
            setImageDrawable(getCircleDrawable(getContext(), resId));
        } else {
            super.setImageResource(resId);

            notifyDrawableChanged();
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable instanceof BitmapDrawable && mCornerRadius > 0) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                super.setImageDrawable(getRoundedDrawable(getContext(), bitmap, mCornerRadius));
            } else {
                super.setImageDrawable(drawable);
            }
        } else if (drawable instanceof BitmapDrawable && mIsCircle) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                super.setImageDrawable(getCircleDrawable(getContext(), bitmap));
            } else {
                super.setImageDrawable(drawable);
            }
        } else {
            super.setImageDrawable(drawable);
        }

        notifyDrawableChanged();
    }

    private void notifyDrawableChanged() {
        if (mOnDrawableChangedCallback != null) {
            mOnDrawableChangedCallback.onDrawableChanged();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsCircle || mIsSquare) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBorderWidth > 0) {
            if (mIsCircle) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mBorderWidth / 2, mBorderPaint);
            } else {
                canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mCornerRadius, mCornerRadius, mBorderPaint);
            }
        }
    }

    public void setDrawableChangedCallback(OnDrawableChangedCallback onDrawableChangedCallback) {
        mOnDrawableChangedCallback = onDrawableChangedCallback;
    }

    public interface OnDrawableChangedCallback {
        void onDrawableChanged();
    }

    public static RoundedBitmapDrawable getCircleDrawable(Context context, Bitmap bitmap) {
        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        circleDrawable.setAntiAlias(true);
        circleDrawable.setCornerRadius(Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        return circleDrawable;
    }

    public static RoundedBitmapDrawable getCircleDrawable(Context context, @DrawableRes int resId) {
        return getCircleDrawable(context, BitmapFactory.decodeResource(context.getResources(), resId));
    }

    public static RoundedBitmapDrawable getRoundedDrawable(Context context, Bitmap bitmap, float cornerRadius) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedBitmapDrawable.setAntiAlias(true);
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        return roundedBitmapDrawable;
    }

    public static RoundedBitmapDrawable getRoundedDrawable(Context context, @DrawableRes int resId, float cornerRadius) {
        return getRoundedDrawable(context, BitmapFactory.decodeResource(context.getResources(), resId), cornerRadius);
    }
}
