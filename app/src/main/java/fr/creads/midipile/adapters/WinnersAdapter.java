package fr.creads.midipile.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.objects.Deal;
import fr.creads.midipile.objects.User;

/**
 * Author : Hugo Gresse
 * Date : 28/08/14
 */
public class WinnersAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater; // inflater pour charger le XML pour l'item
    private ArrayList<Deal> dealItems;
    private Deal mCurrentDeal;

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;

    SimpleDateFormat mySQLDateFormatter;
    Calendar calendar ;
    SimpleDateFormat dealDateFormat;

    public WinnersAdapter(Context context, ArrayList<Deal> dealItems){
        this.context = context;
        this.dealItems = dealItems;

        imageLoader = ImageLoader.getInstance();
        imageLoaderDisplayOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true)
                .build();

        mySQLDateFormatter = new SimpleDateFormat( "yyyy-MM-dd", java.util.Locale.getDefault());
        calendar = Calendar.getInstance();

        dealDateFormat = new SimpleDateFormat( "d MMMM", Locale.getDefault() );
    }

    @Override
    public int getCount() {
        return dealItems.size();
    }
    @Override
    public Object getItem(int position) {
        return dealItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_winner, null);
        }

        mCurrentDeal = dealItems.get(position);
        ImageView image = (ImageView) convertView.findViewById(R.id.dealImageView);
        if(!mCurrentDeal.getImages().isEmpty()){
            String url = mCurrentDeal.getImages().get(0);
            imageLoader.displayImage(url, image, imageLoaderDisplayOptions);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.dealTitle);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView winnerTextView = (TextView) convertView.findViewById(R.id.winnerTextView);
        TextView winnersTextView = (TextView) convertView.findViewById(R.id.winnersTextView);
        final RelativeLayout winnersInfoRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.winnersInfoRelativeLayout);

        txtName.setText(mCurrentDeal.getNom());

        // set Winners name in the invisible layout
        String winnersString = "";
        for (int i = 0; i < mCurrentDeal.getWinners().size(); i++) {
            User u = mCurrentDeal.getWinners().get(i);
            // stop loop after 5 winners
            if(i >= 5){
                winnersString += context.getString(R.string.winners_more);
                break;
            }
            if( !u.getNom().isEmpty() && !u.getPrenom().isEmpty()){
                winnersString += u.getNom().substring(0, 1).toUpperCase() + ". " +
                                 u.getPrenom() + "\n";
            }
        }
        winnersTextView.setText(winnersString);

        if(mCurrentDeal.getWinnersCount() > 1){
            winnerTextView.setText(Integer.toString(mCurrentDeal.getWinnersCount()) + " " + context.getString(R.string.winners_name));
        } else {
            winnerTextView.setText(Integer.toString(mCurrentDeal.getWinnersCount()) + " " + context.getString(R.string.winner_name));
        }

        Date endDate = null;
        try {
            endDate = mySQLDateFormatter.parse(mCurrentDeal.getDateFin());
            calendar.setTime(endDate);

            dateTextView.setText( context.getString(R.string.winners_of) + " " + dealDateFormat.format( endDate ));
        } catch (Exception e){
            Log.d(Constants.TAG, "Error while parsing winners deal date");
        }



        winnersInfoRelativeLayout.setVisibility(View.INVISIBLE);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(winnersInfoRelativeLayout.getVisibility() == View.VISIBLE){
                    winnersInfoRelativeLayout.setVisibility(View.INVISIBLE);

                } else {
                    winnersInfoRelativeLayout.setVisibility(View.VISIBLE);


                    // send read more event tracking
                    ((MidipileApplication)context).sendEventTracking(
                            R.string.tracker_winners_category,
                            R.string.tracker_winners_action_showname,
                            mCurrentDeal.getNom());
                }
            }

        });


        return convertView;
    }



}
