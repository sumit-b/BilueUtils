package au.com.woolworthslimited.bilueutils.views;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Copyright (c) 2014 Woolworths. All rights reserved.
 */
public class FontTypefaceSpan extends TypefaceSpan {
    private Typeface mTypeface;
    private float mTextSize;
    private int mTextColor;

    public FontTypefaceSpan(Parcel src) {
        super(src);
    }

    public FontTypefaceSpan(String family) {
        super(family);
    }

    public static class Builder {
        private FontTypefaceSpan mSpan;

        public Builder() {
            mSpan = new FontTypefaceSpan("");
        }

        public Builder setTypeface(Typeface typeface) {
            mSpan.mTypeface = typeface;
            return this;
        }

        public Builder setTextSize(float textSize) {
            mSpan.mTextSize = textSize;
            return this;
        }

        public Builder setTextColor(int textColor) {
            mSpan.mTextColor = textColor;
            return this;
        }

        public FontTypefaceSpan create() {
            return mSpan;
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, mTypeface);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, mTypeface);
    }

    private void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setColor(mTextColor);

        if (mTextSize > 0) {
            paint.setTextSize(mTextSize);
        }

        paint.setTypeface(tf);
    }
}