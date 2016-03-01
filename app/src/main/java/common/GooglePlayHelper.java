package common;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by winhtaikaung on 3/1/16.
 */
public class GooglePlayHelper {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static boolean isPlayServiceAvailable(Context c) {

        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(c);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode,c,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(c,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();

            }
            return false;
        }
        return true;
    }

    public static boolean isGPSEnabled(Activity activity){
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    public static float calculateDistance(Location last, Location current){
        //to get KM we have to divide with 1000
        float distance=last.distanceTo(current);
        return distance;
    }
}
