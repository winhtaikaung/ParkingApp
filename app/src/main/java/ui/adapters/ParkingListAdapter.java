package ui.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parking.R;

import java.util.List;

import common.GooglePlayHelper;
import models.Entry;

/**
 * Created by winhtaikaung on 2/29/16.
 */


public class ParkingListAdapter extends RecyclerView.Adapter<ParkingListAdapter.ParkingViewHolder> {

    List<Entry> mEntries;
    Context mContext;
    private ParkingListOnClickHandler mClickHandler;
    private Location mCurrentLocation;

    private int lastPosition = -1;


    public ParkingListAdapter(List<Entry> entries, Context c, Location current, ParkingListOnClickHandler _handler) {
        this.mContext = c;
        this.mEntries = entries;
        this.mClickHandler = _handler;
        this.mCurrentLocation = current;


    }


    @Override
    public ParkingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.parking_rowitem, parent, false);

        return new ParkingViewHolder(itemView);
    }

    /**
     * Add Parking to List
     *
     * @param model
     */
    public void addParking(Entry model) {
        this.mEntries.add(model);
        notifyDataSetChanged();
    }

    /**
     * Swap The Parking List with parking lists
     *
     * @param entries
     */
    public void swaplist(List<Entry> entries) {
        this.mEntries = entries;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ParkingViewHolder holder, int position) {
        Entry parking = mEntries.get(position);


        holder.tv_lotno.setText(parking.getContent().getMproperties().getLots());

        holder.tv_location.setText(parking.getContent().getMproperties().getDevelopment());
        //need to do some calculation here
        if (mCurrentLocation != null) {
            Log.e("LATITUDE", String.valueOf(mCurrentLocation.getLatitude()));
            Log.e("LONGITUDE", String.valueOf(mCurrentLocation.getLongitude()));
            holder.tv_distance.setText(getDistance(parking) + "km");


        }
        setAnimation(holder.list_item_layout, position);


        //Log.e("LOT_NO",parking.getContent().getMproperties().getLots());
        //Log.e("NAME",parking.getContent().getMproperties().getDevelopment());




        //holder.tv_lotno.setText(lat_lon[0]);

    }

    double getDistance(Entry park){
        double lat=Double.parseDouble(park.getContent().getMproperties().getLatitude());
        double lon=Double.parseDouble(park.getContent().getMproperties().getLongitude());
        Location destination=new Location("destination");
        destination.setLatitude(lat);
        destination.setLongitude(lon);

        return round(GooglePlayHelper.calculateDistance(mCurrentLocation,destination)/1000,2);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public interface ParkingListOnClickHandler {
        void onItemClickListener(int AdapterPosition, Entry selectedEntry, ParkingViewHolder vh);


        //void onContactClick(PartyItem item, SdsFormViewHolder vh);
    }


    public class ParkingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_lotno;
        public TextView tv_distance;
        public TextView tv_location;


        public CardView list_item_layout;

        public ParkingViewHolder(View itemView) {
            super(itemView);
            list_item_layout = (CardView) itemView.findViewById(R.id.list_item_layout);
            tv_lotno = (TextView) itemView.findViewById(R.id.tv_lotNo);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);

            list_item_layout.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Entry e = mEntries.get(adapterPosition);
            mClickHandler.onItemClickListener(adapterPosition, e, this);

        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        /*if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }*/
    }


}
