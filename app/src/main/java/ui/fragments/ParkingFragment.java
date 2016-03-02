package ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;
import com.nobrain.android.permissions.Result;
import com.parking.R;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import API.RetrofitAPI;
import common.GlobalValues;
import common.GooglePlayHelper;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import listenerinterface.IGPSChangeListener;
import models.Entry;
import models.Feed;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ui.activities.DetailActivity;
import ui.adapters.ParkingListAdapter;
import ui.services.GPSTracker;

/**
 * Created by winhtaikaung on 2/26/16.
 */
public class ParkingFragment extends Fragment implements IGPSChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE = 102;
    private static final String TAG = "";
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    GPSTracker gpsTracker;
    RecyclerView parkinglist;
    LinearLayoutManager layoutManager;
    ParkingListAdapter parkingListAdapter;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking, container, false);

        if (!GooglePlayHelper.isGPSEnabled(getActivity())) {
            buildAlertMessageNoGps(getActivity());
        }
        if (GooglePlayHelper.isPlayServiceAvailable(getActivity())) {
            buildGoogleApiClient();
            checkPermissions();
            bindView(view);
        }


        return view;
    }

    void bindView(View v) {
        parkinglist = (RecyclerView) v.findViewById(R.id.parking_list);


        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        parkinglist.setLayoutManager(layoutManager);

        //grabdataFromAPI(displayLocationbyPlayservice());

        //Getting data from SHared Preference


    }

    public final BroadcastReceiver ParkingReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("parking")) {
                grabdataFromAPI(mLastLocation);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, final @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndroidPermissions.result(getActivity())
                .addPermissions(REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION)
                .putActions(REQUEST_CODE, new Result.Action0() {
                    @Override
                    public void call() {
                        String msg = "Request Success : " + permissions[0];


                        grabdataFromAPI(displayLocationbyPlayservice());


                    }
                }, new Result.Action1() {
                    @Override
                    public void call(String[] hasPermissions, String[] noPermissions) {
                        String msg = "Request Fail : " + noPermissions[0];
                        /*Toast.makeText(getActivity(),
                                msg,
                                Toast.LENGTH_SHORT).show();*/


                    }
                })
                .result(requestCode, permissions, grantResults);
    }

    void checkPermissions() {
        AndroidPermissions.check(getActivity())
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        Log.d(TAG, msg);
                        /*Toast.makeText(getActivity(),
                                msg,
                                Toast.LENGTH_SHORT).show();*/

                        gpsTracker = new GPSTracker(getActivity(),ParkingFragment.this);
                        grabdataFromAPI(displayLocationbyPlayservice());

                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];
                        Log.d(TAG, msg);
                        /*Toast.makeText(getActivity(),
                                msg,
                                Toast.LENGTH_SHORT).show();*/

                        ActivityCompat.requestPermissions(getActivity()
                                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , REQUEST_CODE);
                    }
                })
                .check();
    }

    private void grabdataFromAPI(final Location lastlocation) {

        RetrofitAPI.getInstance(getActivity()).getService().getParkings(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.i("SUCCESS", s);


                try {
                    XmlParserCreator parserCreator = new XmlParserCreator() {
                        @Override
                        public XmlPullParser createParser() {
                            try {
                                return XmlPullParserFactory.newInstance().newPullParser();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    final GsonXml gsonXml = new GsonXmlBuilder()
                            .setXmlParserCreator(parserCreator)
                            .setSameNameLists(true)
                            .setTreatNamespaces(false)
                            .create();
                    try {
                        String xml=s;
                        //String xml = loadStringFromFile(getActivity(), "demo.xml");
                        xml = xml.replace("m:", "");
                        xml = xml.replace("d:", "");
                        xml = xml.replace("type=\"Edm.Int32\"", "");
                        xml = xml.replace("type=\"Edm.DateTime\"", "");
                        xml = xml.replace("type=\"Edm.Double\"", "");
                        Feed fed = gsonXml.fromXml(xml, Feed.class);



                        parkingListAdapter = new ParkingListAdapter(fed.getEntry(), getActivity(), lastlocation, new ParkingListAdapter.ParkingListOnClickHandler() {
                            @Override
                            public void onItemClickListener(int AdapterPosition, Entry selectedEntry, ParkingListAdapter.ParkingViewHolder vh) {
                                //Toast.makeText(getActivity(),selectedEntry.getContent().getMproperties().getLatitude(),Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), DetailActivity.class);
                                intent.putExtra(GlobalValues.PARKING_NAME, selectedEntry.getContent().getMproperties().getDevelopment());
                                intent.putExtra(GlobalValues.LOT_NO, selectedEntry.getContent().getMproperties().getLots());
                                intent.putExtra(GlobalValues.PARKING_ID, selectedEntry.getContent().getMproperties().getCarParkID());
                                intent.putExtra(GlobalValues.DISTANCE, vh.tv_distance.getText());
                                intent.putExtra(GlobalValues.LONGITUDE, selectedEntry.getContent().getMproperties().getLongitude());
                                intent.putExtra(GlobalValues.LATITUDE, selectedEntry.getContent().getMproperties().getLatitude());


                                startActivity(intent);

                                getActivity().overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
                            }
                        });


                        parkinglist.setAdapter(new SlideInRightAnimationAdapter(parkingListAdapter));


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RETROFIT_ERR", error.getMessage());
            }
        });


    }

    @Override
    public void onConnected(Bundle bundle) {
        grabdataFromAPI(displayLocationbyPlayservice());
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void OnUserMove(Location l) {
        grabdataFromAPI(l);
        //Toast.makeText(getActivity(), "Your Location is - \nLat: " + l.getLatitude() + "\nLong: " + l.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("CONENCTION_FAILED", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)


                .addApi(LocationServices.API).build();
    }


    private Location displayLocationbyPlayservice() {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();


            LatLng location = new LatLng(latitude, longitude);


        } else {

            if (gpsTracker.canGetLocation()) {
                LatLng location = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());


                //} else {

            } else {
                //if GPS couldn't load last known location it will load from Playservice
                //LocationServices.FusedLocationApi.getLastLocation()
                //gpsTracker.showSettingsAlert();

            }
        }
        return mLastLocation;
    }

    public void buildAlertMessageNoGps(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.msg_gps_alert))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        dialog.cancel();
                        getActivity().finish();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("parking");
        LocalBroadcastManager.getInstance(getActivity()).
                registerReceiver(ParkingReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayLocationbyPlayservice();
    }
}
