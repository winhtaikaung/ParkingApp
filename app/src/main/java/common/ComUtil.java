package common;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by winhtaikaung on 3/1/16.
 */
public class ComUtil {
    public static float calculateDistance(Location last, Location current) {
        //to get KM we have to divide with 1000
        float distance = last.distanceTo(current);
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

    public static String loadStringFromFile(Context context, String filepath) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filepath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String purifyxml(String raw){
        raw = raw.replace("m:", "");
        raw = raw.replace("d:", "");
        raw = raw.replace("type=\"Edm.Int32\"", "");
        raw = raw.replace("type=\"Edm.DateTime\"", "");
        raw = raw.replace("type=\"Edm.Double\"", "");
        return raw;
    }
}
