package au.com.woolworthslimited.bilueutils.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Copyright (c) 2015 Woolworths. All rights reserved.
 */
public class AlertDialogFragment extends DialogFragment {
    public final static String TITLE_KEY = "title";
    public final static String MESSAGE_KEY = "message";
    public final static String BACK_KEY = "back_key";
    public final static String NEGATIVE_KEY = "negative_key";

    public interface AlertDialogFragmentClick {
        void onPositiveClicked();
        void onNegativeClicked();
    }

    protected AlertDialogFragmentClick mClickListener;
    protected String mTitle;
    protected String mMessage;
    protected boolean mBackKey;
    protected boolean mNegativeKey;

    private AlertDialog.Builder mBuilder;

    public static AlertDialogFragment newInstance(Bundle bundle) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.mTitle = bundle.getString(TITLE_KEY);
        fragment.mMessage = bundle.getString(MESSAGE_KEY);
        fragment.mBackKey = bundle.getBoolean(BACK_KEY, true);
        fragment.mNegativeKey = bundle.getBoolean(NEGATIVE_KEY);
        return fragment;
    }

    public void setOnDialogButtonClickListener(AlertDialogFragmentClick clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mBuilder = new AlertDialog.Builder(getActivity()).setTitle(mTitle).
                setMessage(mMessage).
                setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissAllowingStateLoss();  // dismiss
                        if(mClickListener != null) {
                            mClickListener.onPositiveClicked();
                        }
                        if (mBackKey) {
                            getActivity().onBackPressed();
                        }
                    }
                });
        if(mNegativeKey) {
            mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(mClickListener != null) {
                        mClickListener.onNegativeClicked();
                    }
                }
            });
        }
        return mBuilder.create();
    }
}
