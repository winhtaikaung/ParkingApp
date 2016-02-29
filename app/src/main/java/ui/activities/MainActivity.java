package ui.activities;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;



import com.parking.R;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import API.RetrofitAPI;
import models.Feed;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ui.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] titles={"FAVOURITES","PARKINGS"};
    private int[] tabIcons = {
            R.drawable.ic_fav,
            R.drawable.ic_parking

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tabLayout=(TabLayout) findViewById(R.id.tabs);
        viewPager=(ViewPager) findViewById(R.id.pager);



        setSupportActionBar(toolbar);

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

        final  ViewPager pager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),titles);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);


        setupTabIcons();






        RetrofitAPI.getInstance(this).getService().getParkings(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.i("SUCCESS",s);
                Object obj=null;

                try {

                    try{
                        String xml=s;
                        xml=xml.replace("m:","");
                        xml=xml.replace("d:","");
                        xml=xml.replace("type=\"Edm.Int32\"","");
                        xml=xml.replace("type=\"Edm.DateTime\"","");
                        xml=xml.replace("type=\"Edm.Double\"","");
                        Feed fed=gsonXml.fromXml(xml, Feed.class);

                        fed.getEntry();
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

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_fav);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_parking);


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
