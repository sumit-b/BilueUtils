package au.com.woolworthslimited.bilueutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.SparseArray;

import au.com.woolworthslimited.bilueutils.views.FontTypefaceSpan;

public class TypefaceManager {
    // normal text
    public static final int FONT_SOURCESANSPRO_REGULAR = 1;
    public static final int FONT_SOURCESANSPRO_ITALIC = 2;
    public static final int FONT_SOURCESANSPRO_SEMI_BOLD = 3;
    public static final int FONT_SOURCESANSPRO_BOLD = 4;

    //price text
    public static final int FONT_OPENSANS_REGULAR = 5;
    public static final int FONT_OPENSANS_ITALIC = 6;
    public static final int FONT_OPENSANS_SEMI_BOLD = 7;
    public static final int FONT_OPENSANS_BOLD = 8;

    public static final int FONT_OPENSANS_LIGHT = 10;
    public static final int FONT_SOURCESANSPRO_LIGHT = 11;

    private static Resources mRes;
    //Typeface cache
    private static final SparseArray<Typeface> sTypefaceCache = new SparseArray<>();

    public static void init(final Context context) {
        mRes = context.getResources();
    }

    public static Typeface getFont(final int customFont, Context context) {
        if (sTypefaceCache.get(customFont) == null) {
            String fontAssetPath = null;

            switch (customFont) {
                case FONT_OPENSANS_REGULAR:
                    fontAssetPath = "fonts/OpenSans-Regular.ttf";

                    break;
                case FONT_OPENSANS_ITALIC:
                    fontAssetPath = "fonts/OpenSans-Italic.ttf";
                    break;
                case FONT_OPENSANS_SEMI_BOLD:
                    fontAssetPath = "fonts/OpenSans-Semibold.ttf";
                    break;
                case FONT_OPENSANS_BOLD:
                    fontAssetPath = "fonts/OpenSans-Bold.ttf";
                    break;
                case FONT_OPENSANS_LIGHT:
                    fontAssetPath = "fonts/OpenSans-Light.ttf";
                    break;
                case FONT_SOURCESANSPRO_REGULAR:
                    fontAssetPath = "fonts/SourceSansPro-Regular.ttf";
                    break;
                case FONT_SOURCESANSPRO_ITALIC:
                    fontAssetPath = "fonts/SourceSansPro-Italic.ttf";
                    break;
                case FONT_SOURCESANSPRO_SEMI_BOLD:
                    fontAssetPath = "fonts/SourceSansPro-Semibold.ttf";
                    break;
                case FONT_SOURCESANSPRO_BOLD:
                    fontAssetPath = "fonts/SourceSansPro-Bold.ttf";
                    break;
                case FONT_SOURCESANSPRO_LIGHT:
                    fontAssetPath = "fonts/SourceSansPro-Light.ttf";
                    break;
            }

            if (!TextUtils.isEmpty(fontAssetPath)) {
                sTypefaceCache.put(customFont, Typeface.createFromAsset(context.getApplicationContext().getResources().getAssets(), fontAssetPath));
            }
        }
        return sTypefaceCache.get(customFont);
    }

    public static CharacterStyle style(int font, @DimenRes int textSizeRes, @ColorRes int textColorRes, Context context) {
        return style(TypefaceManager.getFont(font, context), mRes.getDimensionPixelSize(textSizeRes), mRes.getColor(textColorRes));
    }

    public static CharacterStyle style(int font, float textSize, @ColorInt int textColor, Context context) {
        return style(TypefaceManager.getFont(font, context), textSize, textColor);
    }

    public static CharacterStyle style(Typeface font, float textSize, @ColorInt int textColor) {
        if (font != null) {
            return new FontTypefaceSpan.Builder()
                    .setTypeface(font)
                    .setTextSize(textSize)
                    .setTextColor(textColor)
                    .create();
        }
        return null;
    }
}
