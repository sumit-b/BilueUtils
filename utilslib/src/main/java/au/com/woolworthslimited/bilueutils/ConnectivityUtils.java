package au.com.woolworthslimited.bilueutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  Utility class to get the current network connection for the phone.
 */
public class ConnectivityUtils {

    public enum ConnectionType{
        NONE,
        WIFI,
        MOBILE
    }

    /**
     * Gets whether there is an internet connection, and if so what kind,
     * either {@code ConnectionType.WIFI} for Wifi or {@code ConnectionType.MOBILE} for 3g/4g
     *
     * @return  The {@link com.woolworthslimited.common.ConnectivityUtils.ConnectionType}
     */
    public static ConnectionType getConnectionType(Context context) {

        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo != null && networkInfo.isConnected()) {
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return ConnectionType.WIFI;
            }
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return ConnectionType.MOBILE;
            }
        }

        return ConnectionType.NONE;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }
}
