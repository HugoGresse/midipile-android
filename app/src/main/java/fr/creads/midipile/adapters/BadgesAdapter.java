package fr.creads.midipile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.R;
import fr.creads.midipile.objects.Badge;

/**
 * Author : Hugo Gresse
 * Date : 17/09/14
 */
public class BadgesAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater; // inflater pour charger le XML pour l'item
    private ArrayList<Badge> badgesItems;
    private Badge mCurrentBadge;

    public BadgesAdapter(android.content.Context context, List<Badge> badges){
        this.context = context;
        this.badgesItems = (ArrayList<Badge>) badges;

    }

    @Override
    public int getCount() {
        return badgesItems.size();
    }
    @Override
    public Object getItem(int position) {
        return badgesItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        mCurrentBadge = badgesItems.get(position);

        if (null == convertView) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_badges, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.badgeIconImageView);

        TextView title = (TextView) convertView.findViewById(R.id.badgeTitleTextView);
        TextView description = (TextView) convertView.findViewById(R.id.badgeDescriptionTextView);
        TextView chance = (TextView) convertView.findViewById(R.id.badgeChanceTextView);

        title.setText(mCurrentBadge.getNom());
        description.setText(mCurrentBadge.getDescription());

        chance.setText(Integer.toString(mCurrentBadge.getCredit()));

        return convertView;
    }


}
