package fr.creads.midipile.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.creads.midipile.R;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 28/08/14
 */
public class DealsAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater; // inflater pour charger le XML pour l'item
    private ArrayList<Deal> dealItems;
    private Deal mCurrentDeal;

    ImageLoader imageLoader;
    SimpleDateFormat mySQLDateFormatter;

    Date curDate;

    public DealsAdapter(Context context, ArrayList<Deal> dealItems){
        this.context = context;
        this.dealItems = dealItems;

        imageLoader = ImageLoader.getInstance();
        mySQLDateFormatter = new SimpleDateFormat( "yyyy-MM-dd", java.util.Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        curDate = (Date) cal.getTime();
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
            convertView = mInflater.inflate(R.layout.list_item_deals, null);
        }

        mCurrentDeal = dealItems.get(position);
        ImageView image = (ImageView) convertView.findViewById(R.id.dealImageView);
        if(!mCurrentDeal.getImages().isEmpty()){
            String url = mCurrentDeal.getImages().get(0);
            imageLoader.displayImage(url, image);
        }

        // set loader position
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.dealProgressBar);

        Date startDate = null;
        Date endDate = null;
        int progress = 0;
        Log.i("midipile", mCurrentDeal.getDateLancement());
        try {

            startDate = mySQLDateFormatter.parse(mCurrentDeal.getDateLancement());
            endDate = mySQLDateFormatter.parse(mCurrentDeal.getDateFin());

            int dealDayDuration = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
            int curDayDuration = (int) ((curDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));

            progress = (100 * curDayDuration) / dealDayDuration;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        progressBar.setProgress(progress);

        TextView txtName = (TextView) convertView.findViewById(R.id.dealTitle);

        txtName.setText(mCurrentDeal.getNom());

        return convertView;
    }

}
