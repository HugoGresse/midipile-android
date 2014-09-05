package fr.creads.midipile.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.objects.Deal;

/**
 * Author : Hugo Gresse
 * Date : 03/09/14
 */
public class DealProductFragment extends Fragment {

    ImageLoader imageLoader;
    DisplayImageOptions imageLoaderDisplayOptions;

    private Deal deal;

    private Typeface typefaceLoto;

    private View rootView;
    private ViewFlipper productViewFlipper;
    private TextView dealValueTextView;
    private ProgressBar progressBar;
    private Button participateButton;
    private WebView detailWebView;
    private ScrollView productScrollView;

    private String valueText = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        imageLoader = ImageLoader.getInstance();
        imageLoaderDisplayOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_deal_product, container, false);
            dealValueTextView = (TextView) rootView.findViewById(R.id.dealValue);
            productViewFlipper = (ViewFlipper) rootView.findViewById(R.id.productViewFlipper);
            progressBar = (ProgressBar) rootView.findViewById(R.id.dealProgressBar);
            participateButton = (Button) rootView.findViewById(R.id.participateButton);
            detailWebView = (WebView) rootView.findViewById(R.id.dealDetail);
            productScrollView = (ScrollView) rootView.findViewById(R.id.productScrollView);

            setInsets(getActivity(), productScrollView);

            typefaceLoto = Typeface.createFromAsset( getActivity().getApplicationContext().getAssets(),
                    "fonts/latoregular.ttf");
        } catch (InflateException e) {

        }

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deal = ((HomeActivity)getActivity()).getSelectedDeal();

        participateButton.setTypeface(typefaceLoto);
        progressBar.setProgress(getProgress(deal.getDateLancement(), deal.getDateFin()));

        String color = String.format("#%06X",
                (0xFFFFFF & getActivity().getResources().getColor(R.color.midipiletheme_color)));
        String webviewData = "<html><head><style type=\"text/css\"> " +
                "img { max-width : 100% !important; height : auto !important; } " +
                "a { color : " + color + " ; }" +
                "</style>" + deal.getDescription() ;

        detailWebView.loadData(webviewData, "text/html", "utf-8");

        valueText = getActivity().getResources().getString(R.string.deal_value_prefix);
        valueText += " " + Float.toString(deal.getValeur()) + "€ (";
        valueText += Integer.toString(deal.getQuantite()) + " " + getActivity().getResources().getString(R.string.deal_value_suffix) + " )";

        dealValueTextView.setText(valueText);


        productViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.fade_in));
        productViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.fade_out));

        if(!deal.getImages().isEmpty()) {
            for (String url : deal.getImages()){
                ImageView imageView = new ImageView(getActivity());
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageLoader.displayImage(url, imageView, imageLoaderDisplayOptions);
                productViewFlipper.addView(imageView);
            }
            if(deal.getImages().size() > 1) {
                productViewFlipper.setAutoStart(true);
                productViewFlipper.startFlipping();
            }
        }


    }

    public int getProgress(String dateLancement, String dateFin){
        SimpleDateFormat mySQLDateFormatter = new SimpleDateFormat( "yyyy-MM-dd", java.util.Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        Date curDate = (Date) cal.getTime();

        Date startDate = null;
        Date endDate = null;
        int progress = 0;
        try {
            startDate = mySQLDateFormatter.parse(dateLancement);
            endDate = mySQLDateFormatter.parse(dateFin);

            int dealDayDuration = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
            int curDayDuration = (int) ((curDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));

            progress = (100 * curDayDuration) / dealDayDuration;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return progress;
    }


    public static void setInsets(Activity context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, 0, 0, config.getNavigationBarHeight());
    }

}
