package ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.parking.R;

import common.GlobalValues;

/**
 * Created by winhtaikaung on 3/1/16.
 */
public class DetailViewFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    IconGenerator iconFactory;
    Button btn_fav;
    TextView txt_lotname,txt_lotno,txt_distance;

    String parking_name, lotno, distance, longitude, latitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        txt_lotname=(TextView) v.findViewById(R.id.tv_location);
        txt_lotno=(TextView) v.findViewById(R.id.tv_lotNo);
        txt_distance=(TextView) v.findViewById(R.id.tv_distance);
        btn_fav=(Button)v.findViewById(R.id.btn_favourite);
        Bundle b = getArguments();
        if (b != null) {
            parking_name = b.getString(GlobalValues.PARKING_NAME);
            lotno = b.getString(GlobalValues.LOT_NO);
            distance = b.getString(GlobalValues.DISTANCE);
            longitude = b.getString(GlobalValues.LONGITUDE);
            latitude = b.getString(GlobalValues.LATITUDE);

            txt_lotname.setText(parking_name);
            txt_lotno.setText(lotno);
            txt_distance.setText(distance);

        }


        iconFactory = new IconGenerator(getActivity());
        //Setting Bubble color and text
        iconFactory.setColor(getActivity().getResources().getColor(R.color.lot_no_color));
        buildGoogleApiClient();
        iconFactory.setTextAppearance(R.style.TV_LOT_NO);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_fav.setOnClickListener(new OnViewCLickListener());


        return v;
    }

    protected class OnViewCLickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.btn_favourite){
                Toast.makeText(getActivity(),"Favourite list added",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        displaymapwithLocation(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude)), lotno);


    }

    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mapFragment.getMap().addMarker(markerOptions);
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })

                .addApi(LocationServices.API).build();
    }

    void displaymapwithLocation(LatLng latLng, String lot_no) {
        mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 18));
        addIcon(iconFactory,lot_no, latLng);
    }
}
