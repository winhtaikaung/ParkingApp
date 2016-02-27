package ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.parking.R;

import API.RetrofitAPI;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()==null){
            getSupportActionBar().setTitle("LOL");
        }

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
}
