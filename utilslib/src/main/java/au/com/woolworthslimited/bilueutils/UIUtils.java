package au.com.woolworthslimited.bilueutils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import au.com.woolworthslimited.bilueutils.ui.AlertDialogFragment;

/**
 * Copyright (c) 2014 Woolworths. All rights reserved.
 */
public class UIUtils {
    private static Interpolator mAccelerator = new AccelerateInterpolator();
    private static Interpolator mDecelerator = new DecelerateInterpolator();

    /**
     * Flips an object 180 degree
     *
     * @param viewToFlip       The view to flip
     * @param animatorListener A listener to be notified of animation events
     */
    public static void flipView(View viewToFlip, final Animator.AnimatorListener animatorListener) {
        ObjectAnimator flipToEdge = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 90f);
        flipToEdge.setDuration(300);
        flipToEdge.setInterpolator(mAccelerator);
        final ObjectAnimator flipBackFlat = ObjectAnimator.ofFloat(viewToFlip, "rotationY", -90f, 0f);
        flipBackFlat.setDuration(300);
        flipBackFlat.setInterpolator(mDecelerator);
        flipToEdge.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                flipBackFlat.start();
                if (animatorListener != null) {
                    flipBackFlat.addListener(animatorListener);
                }
            }
        });
        flipToEdge.start();
    }

    public static void hideIME(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = context.getWindow().getCurrentFocus();
        if (v instanceof EditText) {
            v.clearFocus();
        }
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showIME(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static float convertSpToPixel(Context context, int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, context.getResources().getDisplayMetrics());
    }

    public static float convertDpToPixel(Context context, int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources().getDisplayMetrics());
    }

    public static Bitmap changeBitmapColor(Bitmap srcBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        //http://android-er.blogspot.com.au/2012/09/convert-imageview-to-black-and-white.html
        float r = (float) (((color & 0xFF0000) >> 16) / 255.0);
        float g = (float) (((color & 0x00FF00) >> 8) / 255.0);
        float b = (float) (((color & 0x0000FF) >> 0) / 255.0);
        float[] matrix = {
                r, r, r, 0, 0, // R
                g, g, g, 0, 0, // G
                b, b, b, 0, 0, // B
                0, 0, 0, 1, 0}; //A

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(srcBitmap, new Matrix(), paint);
        return resultBitmap;
    }

    public static void showDialog(Context context, final String title, final String mesg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(mesg)
                .setPositiveButton("Okay", listener)
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static Drawable getDrawableFromTheme(Context context, int[] attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        Drawable drawable = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawable;
    }

    public static Drawable getColorDrawable(int color, int height) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(color);
        drawable.setIntrinsicHeight(height);
        return drawable;
    }

    public static void composeEmail(Context context, String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * returns current active fragment.
     *
     * @param manager
     * @return
     */
    public static Fragment getActiveFragment(FragmentManager manager) {
        try {
            for (int i = manager.getFragments().size() - 1; i >= 0; i--) {
                Fragment fragment = manager.getFragments().get(i);
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Point getScreenSize(Context context) {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(size);
        return size;
    }

    public static void showAlertDialog(FragmentManager fragmentManager, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(AlertDialogFragment.MESSAGE_KEY, message);
        bundle.putBoolean(AlertDialogFragment.BACK_KEY, false);
        DialogFragment dialogFragment = AlertDialogFragment.newInstance(bundle);
        dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
    }
}