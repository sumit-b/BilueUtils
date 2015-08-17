package au.com.woolworthslimited.bilueutils;

import android.content.Context;

import java.io.IOException;
import java.util.Properties;

import au.com.woolworthslimited.bilueutils.exceptions.PropertyUtilException;

public class PropertyUtil {
    private static Properties sProperties;

    public static final String BASE_URL_V1 = "base.url.v1";
    public static final String BASE_URL_V2 = "base.url.v2";
    public static final String BASE_URL_V3 = "base.url.v3";
    public static final String API_KEY = "apikey";
    public static final String HOCKEYAPP_ID = "hockeyapp.id";
    public static final String APIGEE_BASE_URL = "apigee.base";
    public static final String AUTOCOMPLETE_URL = "autocomplete.url";
    public static final String PASSWORD_FORGOT_URL = "password.url";

    public PropertyUtil(Context context, int rawResId) throws PropertyUtilException {
        if (sProperties == null) {
            sProperties = new Properties();
            try {
                sProperties.load(context.getResources().openRawResource(rawResId));
                //sProperties.load(context.getResources().openRawResource(R.raw.config));
            } catch (IOException e) {
                throw new PropertyUtilException("Could not load config.properties file: " + e.getMessage());
            }
        }
    }

    public static String getProperty(String name) {
        return sProperties.getProperty(name);
    }
}