package ui.activities;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.parking.R;

import API.RetrofitAPI;
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


        final  ViewPager pager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),titles);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);


        setupTabIcons();




        RetrofitAPI.getInstance(this).getService().getParkings(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.i("SUCCESS",s);
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


}
