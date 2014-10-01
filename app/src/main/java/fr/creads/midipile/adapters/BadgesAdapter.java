package fr.creads.midipile.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.objects.Badge;

/**
 * Author : Hugo Gresse
 * Date : 17/09/14
 */
public class BadgesAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater; // inflater pour charger le XML pour l'item
    private ArrayList<Badge> badgesItems;
    private Badge mCurrentBadge;

    private ColorFilter colorFilter;
    private Typeface latoTypeface;

    public BadgesAdapter(Activity context, List<Badge> badges){
        this.context = context;
        this.badgesItems = (ArrayList<Badge>) badges;


        int iColor = Color.parseColor("#ffffff");

        int red = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue = iColor & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0 };
        colorFilter = new ColorMatrixColorFilter(matrix);

        latoTypeface =((HomeActivity) context).getLatoTypeface();
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

    // disable item selection
    @Override
    public boolean isEnabled(int position){
        return false;
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
        Button fbFanButton = (Button) convertView.findViewById(R.id.fbFanBadgeButton);

        if(mCurrentBadge.getId() <= 3){
            image.setImageDrawable( context.getResources().getDrawable(R.drawable.ic_badgeparrainage) );
        } else if(mCurrentBadge.getId() == 4){
            image.setImageDrawable( context.getResources().getDrawable(R.drawable.ic_badgelike) );
        }

        if(mCurrentBadge.isUserBadge()){
            ((ListView)parent).setItemChecked(position, true);
            image.setColorFilter(colorFilter);
            fbFanButton.setVisibility(View.GONE);
        } else if(mCurrentBadge.getId().equals(4)){
            Log.d(Constants.TAG, "set button");
            fbFanButton.setVisibility(View.VISIBLE);
            fbFanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeActivity)context).logFbAndCheckPermissionsForLikes();
                }
            });
        }


        TextView title = (TextView) convertView.findViewById(R.id.badgeTitleTextView);
        TextView description = (TextView) convertView.findViewById(R.id.badgeDescriptionTextView);
        TextView chance = (TextView) convertView.findViewById(R.id.badgeChanceTextView);

        title.setTypeface(latoTypeface);
        description.setTypeface(latoTypeface);
        chance.setTypeface(latoTypeface);

        title.setText(mCurrentBadge.getNom());
        description.setText(mCurrentBadge.getDescription());
        chance.setText( "+" +  Integer.toString(mCurrentBadge.getCredit()) + " chances !");

        return convertView;
    }


}
