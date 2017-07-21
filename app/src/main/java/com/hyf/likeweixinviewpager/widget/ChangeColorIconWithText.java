package com.hyf.likeweixinviewpager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hyf.likeweixinviewpager.R;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ChangeColorIconWithText extends View {

    private int color_select = Color.parseColor("#333333");
    private int color_unselect = Color.parseColor("#888888");
    private Bitmap mIconBitmap;
    private String text = "微信";
    private int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;
    private float mAlpha;

    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;


    public ChangeColorIconWithText(Context context) {
        super(context, null);
    }

    public ChangeColorIconWithText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconWithText);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorIconWithText_icon:
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) typedArray.getDrawable(attr);
                    mIconBitmap = bitmapDrawable.getBitmap();
                    break;
                case R.styleable.ChangeColorIconWithText_color_select:
                    color_select = typedArray.getColor(attr, color_select);
                    break;
                case R.styleable.ChangeColorIconWithText_color_unselect:
                    color_unselect = typedArray.getColor(attr, color_unselect);
                    break;
                case R.styleable.ChangeColorIconWithText_text:
                    text = typedArray.getString(attr);
                    break;
                case R.styleable.ChangeColorIconWithText_text_size:
                    textSize = (int) typedArray.getDimension(attr, textSize);
                    break;
            }
        }

        typedArray.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(color_unselect);

        mTextPaint.getTextBounds(text, 0, text.length(), mTextBound);
    }

    public ChangeColorIconWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height());
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - (iconWidth + mTextBound.height()) / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha);
        setUpTargetBitmap(alpha);
        drawSourseText(canvas, alpha);
        drawTargetText(canvas,alpha);

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(color_select);
        mTextPaint.setAlpha(alpha);
        canvas.drawText(text, getMeasuredWidth() / 2 - mTextBound.width() / 2, mIconRect.bottom + mTextBound.height(), mTextPaint);
    }

    private void drawSourseText(Canvas canvas, int alpha) {
        mTextPaint.setColor(color_unselect);
        mTextPaint.setAlpha(255 - alpha);
        canvas.drawText(text, getMeasuredWidth() / 2 - mTextBound.width() / 2, mIconRect.bottom + mTextBound.height(), mTextPaint);
    }

    private void setUpTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(color_select);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }

    public void setIconAlpha(float alpha){
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }else {
            postInvalidate();
        }
    }
}
