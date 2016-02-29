package ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.parking.R;

import java.util.List;

import models.Entry;

/**
 * Created by winhtaikaung on 2/29/16.
 */


public class ParkingListAdapter extends RecyclerView.Adapter<ParkingListAdapter.ParkingViewHolder> {

    List<Entry> mEntries;
    Context mContext;

    private int lastPosition = -1;



    public ParkingListAdapter(List<Entry> entries, Context c){
        this.mContext=c;
        this.mEntries=entries;


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
     * @param model
     */
    public void addParking(Entry model){
        this.mEntries.add(model);
        notifyDataSetChanged();
    }

    /**
     * Swap The Parking List with parking lists
     * @param entries
     */
    public void swaplist(List<Entry> entries){
        this.mEntries=entries;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ParkingViewHolder holder, int position) {
        Entry parking=mEntries.get(position);



        holder.tv_lotno.setText(parking.getContent().getMproperties().getLots());
        holder.tv_distance.setText("123"+"km");
        holder.tv_location.setText(parking.getContent().getMproperties().getDevelopment());
        //need to do some calculation here

        setAnimation(holder.list_item_layout, position);


        Log.e("LOT_NO",parking.getContent().getMproperties().getLots());
        Log.e("NAME",parking.getContent().getMproperties().getDevelopment());

        //holder.tv_lotno.setText(lat_lon[0]);

    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }


    class ParkingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView tv_lotno;
        protected TextView tv_distance;
        protected TextView tv_location;


        protected CardView list_item_layout;

        public ParkingViewHolder(View itemView){
            super(itemView);
            list_item_layout=(CardView) itemView.findViewById(R.id.list_item_layout);
            tv_lotno =(TextView) itemView.findViewById(R.id.tv_lotNo);
            tv_distance =(TextView) itemView.findViewById(R.id.tv_distance);
            tv_location =(TextView) itemView.findViewById(R.id.tv_location);

        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        /*if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }*/
    }




}
