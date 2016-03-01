package ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.parking.R;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import API.RetrofitAPI;
import common.GlobalValues;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import models.Entry;
import models.Feed;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ui.activities.DetailActivity;
import ui.adapters.ParkingListAdapter;

/**
 * Created by winhtaikaung on 2/26/16.
 */
public class ParkingFragment extends Fragment {


    RecyclerView parkinglist;
    LinearLayoutManager layoutManager;
    ParkingListAdapter parkingListAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_parking,container,false);
        bindView(view);
        return view;
    }

    void bindView(View v){
        parkinglist=(RecyclerView) v.findViewById(R.id.parking_list);


        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        parkinglist.setLayoutManager(layoutManager);

        //Getting data from SHared Preference
        grabdataFromAPI();





    }

   private void  grabdataFromAPI(){

       RetrofitAPI.getInstance(getActivity()).getService().getParkings(new Callback<String>() {
           @Override
           public void success(String s, Response response) {
               Log.i("SUCCESS",s);


               try {
                   XmlParserCreator parserCreator=new XmlParserCreator() {
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
                   try{
                       //String xml=s;
                       String xml=loadStringFromFile(getActivity(),"demo.xml");
                       xml=xml.replace("m:","");
                       xml=xml.replace("d:","");
                       xml=xml.replace("type=\"Edm.Int32\"","");
                       xml=xml.replace("type=\"Edm.DateTime\"","");
                       xml=xml.replace("type=\"Edm.Double\"","");
                       Feed fed=gsonXml.fromXml(xml, Feed.class);

                       parkingListAdapter = new ParkingListAdapter(fed.getEntry(), getActivity(), new ParkingListAdapter.ParkingListOnClickHandler() {
                           @Override
                           public void onItemClickListener(int AdapterPosition, Entry selectedEntry, ParkingListAdapter.ParkingViewHolder vh) {
                               //Toast.makeText(getActivity(),selectedEntry.getContent().getMproperties().getLatitude(),Toast.LENGTH_LONG).show();
                               Intent intent=new Intent(getActivity(), DetailActivity.class);
                               intent.putExtra(GlobalValues.PARKING_NAME,selectedEntry.getContent().getMproperties().getDevelopment());
                               intent.putExtra(GlobalValues.LOT_NO,selectedEntry.getContent().getMproperties().getLots());
                               intent.putExtra(GlobalValues.DISTANCE,"distance");
                               intent.putExtra(GlobalValues.LONGITUDE,selectedEntry.getContent().getMproperties().getLongitude());
                               intent.putExtra(GlobalValues.LATITUDE,selectedEntry.getContent().getMproperties().getLatitude());
                               startActivity(intent);
                           }
                       });

//                       for(Entry e:fed.getEntry()){
//                           parkingListAdapter.addParking(e);
//                       }

                       parkinglist.setAdapter(new SlideInRightAnimationAdapter(parkingListAdapter));


                   }catch (Exception ex){
                       ex.printStackTrace();
                   }

               }catch (Exception ex){
                   ex.printStackTrace();
               }





               //assertEquals(xmlmapper.writeValueAsString(p2),xml);




           }

           @Override
           public void failure(RetrofitError error) {
               Log.e("RETROFIT_ERR",error.getMessage());
           }
       });



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


}
