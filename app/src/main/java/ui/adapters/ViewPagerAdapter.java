package ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import ui.fragments.FavouriteFragment;
import ui.fragments.ParkingFragment;

/**
 * Created by winhtaikaung on 2/27/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    String[] mtitles;
    FragmentManager fragManager;
    FragmentTransaction ft;
    public ViewPagerAdapter(FragmentManager fm,String[] titles) {
        super(fm);
        this.fragManager=fm;
        this.ft=fragManager.beginTransaction();

        this.mtitles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position){
            case 0:
                return new FavouriteFragment();

            case 1:

                return new ParkingFragment();



        }
        return null;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mtitles[position];
    }
}
