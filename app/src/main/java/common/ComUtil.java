package common;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by winhtaikaung on 3/1/16.
 */
public class ComUtil {
    public static float calculateDistance(Location last,Location current){
        //to get KM we have to divide with 1000
        float distance=last.distanceTo(current);
        return distance;
    }

    public static boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}
