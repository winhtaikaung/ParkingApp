package ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.parking.R;

import common.GlobalValues;
import ui.fragments.DetailViewFragment;

/**
 * Created by winhtaikaung on 3/1/16.
 */
public class DetailActivity extends AppCompatActivity {
    String parking_name,lotno,distance,longitude,latitude,carparkid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        parking_name= getIntent().getStringExtra(GlobalValues.PARKING_NAME);
        lotno=getIntent().getStringExtra(GlobalValues.LOT_NO);
        distance=getIntent().getStringExtra(GlobalValues.DISTANCE);
        longitude=getIntent().getStringExtra(GlobalValues.LONGITUDE);
        latitude=getIntent().getStringExtra(GlobalValues.LATITUDE);
        carparkid=getIntent().getStringExtra(GlobalValues.PARKING_ID);

        if(savedInstanceState==null){
            Bundle bundle=new Bundle();
            bundle.putString(GlobalValues.PARKING_NAME,parking_name);
            bundle.putString(GlobalValues.LOT_NO,lotno);
            bundle.putString(GlobalValues.DISTANCE,distance);
            bundle.putString(GlobalValues.LONGITUDE,longitude);
            bundle.putString(GlobalValues.LATITUDE,latitude);
            bundle.putString(GlobalValues.PARKING_ID,carparkid);
            DetailViewFragment dv_fragment=new DetailViewFragment();
            dv_fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.content,dv_fragment ).commit();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}
