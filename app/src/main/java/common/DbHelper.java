package common;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import models.Entry;

/**
 * Created by winhtaikaung on 3/2/16.
 */
public class DbHelper {
    Context mContext;

    public DbHelper(Context c) {
        this.mContext = c;
    }

    public String[] getAllCarparks() {
        String[] arr = new String[0];
        String carparks = MySharedPreference.getInstance(mContext).getStringPreference(GlobalValues.PARKING_DB, "");
        Log.w("CARPARK",carparks);
        if (carparks.equals("")) {
            arr = new String[0];
        }

        if (!carparks.equals("")) {
            arr = carparks.split(",");
        }

        return arr;

    }

    /***
     * Method to add Carpark in Shared Preference
     *
     * @param parkingid
     */
    public void addCarpark(String parkingid) {
        // add destination to string array and set it to preference
        String db = "";
        String location = MySharedPreference.getInstance(mContext).getStringPreference(GlobalValues.PARKING_DB, "");
        //Lat Long
        Log.e("DEST", "");
        if (location.equals("")) {
            db = parkingid + ",";
        } else {
            String[] sList = location.split(",");

            for (String s : sList) {
                db += s + ",";
            }
            db += parkingid + ",";

        }
        MySharedPreference.getInstance(mContext).setStringPreference(GlobalValues.PARKING_DB, db);
    }

    public boolean checkDuplicateCarpark(String parkingid){
        boolean duplicate=false;
        parkingid=parkingid.trim();
        List<String> carparks=Arrays.asList(getAllCarparks());
        int index= Collections.binarySearch(carparks,parkingid);
        if(index==-1){
            duplicate= false;
        }else if(index>-1){

            duplicate= true;
        }
        return  duplicate;
    }

   public List<Entry> getFavouriteEntry(List<Entry> list){
        List<Entry> fav_list=new ArrayList<>();
        for(Entry item:list){
            if(checkisfav(item.getContent().getMproperties().getCarParkID())){
                fav_list.add(item);
            }
        }
        return fav_list;
    }

    public boolean checkisfav(String parkingid){
        boolean fav=false;

        parkingid=parkingid.trim();
        List<String> arr_parking=new ArrayList<>();

        arr_parking=Arrays.asList(getAllCarparks());

        if(arr_parking.contains(parkingid)){
            fav=true;
        }else {
            fav=false;
        }
        return fav;
    }
}
