package fr.creads.midipile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import fr.creads.midipile.R;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 28/08/14
 */
public class WhishlistAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater; // inflater pour charger le XML pour l'item
    private ArrayList<Deal> dealItems;
    private Deal mCurrentDeal;

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;

    public WhishlistAdapter(Context context, ArrayList<Deal> dealItems){
        this.context = context;
        this.dealItems = dealItems;

        imageLoader = ImageLoader.getInstance();
        imageLoaderDisplayOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true)
                .build();
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
            convertView = mInflater.inflate(R.layout.list_item_whishlist, null);
        }

        mCurrentDeal = dealItems.get(position);
        ImageView image = (ImageView) convertView.findViewById(R.id.dealImageView);
        if(!mCurrentDeal.getImages().isEmpty()){
            String url = mCurrentDeal.getImages().get(0);
            imageLoader.displayImage(url, image, imageLoaderDisplayOptions);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.dealTitle);

        txtName.setText(mCurrentDeal.getNom());

        return convertView;
    }

}
