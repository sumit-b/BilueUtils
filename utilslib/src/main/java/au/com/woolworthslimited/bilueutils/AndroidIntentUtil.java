package au.com.woolworthslimited.bilueutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.MailTo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Billy Chang on 23/07/15.
 * Copyright (c) 2015 Woolworths. All rights reserved.
 */
public class AndroidIntentUtil {

    /**  ------------------------------------------------------------------------  Phone Calls -- */

    public static boolean deviceCanMakePhoneCall(final Context context){
        if (((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number() == null){
            // no phone
            return false;
        }
        return true;
    }

    public static Intent newCallNumberIntent(String number){
        return new Intent(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
    }

    public static void launchCallNumberActivity(final Activity activity, String number) {
        try {
            activity.startActivity(AndroidIntentUtil.newCallNumberIntent(number));
        }
        catch (ActivityNotFoundException e){
            new AlertDialog.Builder(activity)
                    .setMessage(R.string.error_device_cant_dial)
                    .setNeutralButton(R.string.ok, null)
                    .show();
        }
    }


    /**  -----------------------------------------------------------------------  External URL -- */

    public static Intent newExternalUrlIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    /**  ------------------------------------------------------------------------------  Email -- */

    public static boolean deviceCanHandleSendTo(final Context context) {
        return isIntentAvailable(context, Intent.ACTION_SENDTO);
    }

    public static Intent newSendToIntent(String emailAddress, String subject, String contentBody) {
        return new Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse(
                        "mailto:" + Uri.encode(emailAddress) +
                                "?subject=" + Uri.encode(subject) +
                                "&body=" + Uri.encode(contentBody)
                ));
    }

    public static void launchSendMailToActivity(final Context context, final String emailAddress, final String subject, final String contentBody) {
        try {
            context.startActivity(AndroidIntentUtil.newSendToIntent(emailAddress, subject, contentBody));
        }
        catch (ActivityNotFoundException e){
            new AlertDialog.Builder(context)
                    .setMessage(R.string.error_device_cant_email)
                    .setNeutralButton(R.string.ok, null)
                    .show();
        }
    }

    public static Intent newSendMultipleAttachmentsIntent(String emailAddress, String subject, String contentBody, ArrayList<Uri> uris) {

        final Intent ei = new Intent(Intent.ACTION_SEND_MULTIPLE);
        ei.setType("plain/text");
        ei.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        ei.putExtra(Intent.EXTRA_SUBJECT, subject);

        //ei.putExtra(Intent.EXTRA_TEXT, contentBody);
        //fix for ClassCastException with Intent.EXTRA_TEXT : https://code.google.com/p/android/issues/detail?id=38303
        //: use list of string not a string
        ArrayList<String> extra_text = new ArrayList<String>();
        extra_text.add(contentBody);
        ei.putExtra(Intent.EXTRA_TEXT, extra_text);

        ei.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        return ei;
    }

    /**  -----------------------------------------------------------------------  Play Store -- */

    public static Intent newOpenStoreIntent(String packageName) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
    }

    /** -----------------------------------------------------------------------  Google Maps -- */

    public static Intent newOpenMapsAtLatLongAndName(String latitude, String longitude, String name) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(Locale.ENGLISH, "geo:%s,%s", latitude, longitude) +
                        "?q=" + Uri.encode(latitude + "," + longitude + "(" + name + ")") +
                        "&z=16"));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        return intent;
    }

    /**  -----------------------------------------------------------------------  Handle All -- */

    public static boolean handleUrlProtocol(final Context context, final String url){
        if (url.startsWith("tel:")) {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            context.startActivity(intent);
            return true;
        }
        else if (url.startsWith("http:")
                || url.startsWith("https:")) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            return true;
        }
        else if (url.startsWith("mailto:")) {
            MailTo mt=MailTo.parse(url);
            launchSendMailToActivity(context, mt.getTo(), mt.getSubject(), mt.getBody());
            return true;
        }

        return false;
    }


    /**
     *
     * This part taken from: http://android-developers.blogspot.com.au/2009/01/can-i-use-this-intent.html
     *
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param action The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
