package fr.creads.midipile.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.regex.Pattern;

import fr.creads.midipile.MidipileApplication;
import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 03/09/14
 */
public class DealBrandFragment extends Fragment {

    private static final String SCREEN_NAME = DealFragment.SCREEN_NAME + "brand/";

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;

    private Deal deal;

    private TextView dealSocieteTitleTextView;
    private ImageView imageView;
    private ImageButton fbButton;
    private ImageButton twButton;
    private ImageButton webButton;
    private WebView productWebView;
    private ScrollView brandScrollView;

    private Pattern pattern;
    private String twitterUser;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        pattern = Pattern.compile("(?<=http[s]?://twitter.com/)($|([^/]*)|\\\\?=)");

        imageLoader = ImageLoader.getInstance();
        imageLoaderDisplayOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deal_brand, container, false);

        dealSocieteTitleTextView = (TextView) rootView.findViewById(R.id.dealSocieteTitleTextView);
        imageView = (ImageView) rootView.findViewById(R.id.brandImageView);
        brandScrollView = (ScrollView) rootView.findViewById(R.id.brandScrollView);

        productWebView = (WebView) rootView.findViewById(R.id.productWebView);
        fbButton = (ImageButton) rootView.findViewById(R.id.fbButton);
        twButton = (ImageButton) rootView.findViewById(R.id.twButton);
        webButton = (ImageButton) rootView.findViewById(R.id.webButton);

        setInsets(getActivity(), brandScrollView);

        return rootView;
    }

    @Override
    public void onResume (){
        super.onResume();
        ((MidipileApplication)getActivity().getApplication()).sendScreenTracking(SCREEN_NAME + deal.getNom());
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deal = ((HomeActivity)getActivity()).getSelectedDeal();

        dealSocieteTitleTextView.setText(deal.getSociete());


        String color = String.format("#%06X", (0xFFFFFF & getActivity().getResources().getColor(R.color.midipiletheme_color)));
        String webviewData = "<html><head><style type=\"text/css\"> img { max-width : 100% !important; height : auto !important; } " +
                "a { color : " + color + " ; }" +
                "</style>" + deal.getDescriptionSociete() ;
        productWebView.loadData(webviewData, "text/html", "utf-8");

        if(!deal.getLogo().isEmpty()){
            String url = deal.getLogo();
            imageLoader.displayImage(url, imageView, imageLoaderDisplayOptions);
        }
        setShareButton();
    }

    private void setShareButton() {

        if(null != deal.getFacebook() && !deal.getFacebook().isEmpty()){
            fbButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String facebookUrl = deal.getFacebook();
                    try {
                        int versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                        if (versionCode >= 3002850) {
                            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        } else {
                            // open the Facebook app using the old method (fb://profile/id or fb://pro
                            if(null != deal.getFbAppId()){
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + deal.getFbAppId()));
                                startActivity(intent);
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        // Facebook is not installed. Open the browser
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                    }
                }
            });
        } else {
            fbButton.setVisibility(View.GONE);
        }

        if(null != deal.getTwitter() && !deal.getTwitter().isEmpty()){
            twButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
//                    try {
//                        Matcher matcher = pattern.matcher(deal.getTwitter());
//                        while (matcher.find()) {
//                            twitterUser = matcher.group();
//                            break;
//                        }
//                        // get the Twitter app if possible
//                        getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
//                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deal.getTwitter()));
//                    }
                    startActivity(intent);
                }
            });
        } else {
            twButton.setVisibility(View.GONE);
        }

        if(null != deal.getWebsite() && !deal.getWebsite().isEmpty()){
            webButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(deal.getWebsite())));
                }
            });
        } else {
            webButton.setVisibility(View.GONE);
        }

    }

    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }
}
