package fr.creads.midipile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.creads.midipile.R;
import fr.creads.midipile.activities.HomeActivity;
import fr.creads.midipile.api.Constants;
import fr.creads.midipile.listeners.OnDataLoadedListener;
import fr.creads.midipile.utilities.MidipileUtilities;

/**
 * Generic content fragment
 *
 * Author : Hugo Gresse
 * Date : 02/10/14
 */
public class ContentFragment extends Fragment implements OnDataLoadedListener {

    public static final String ARGS_CONTENTID = "contentId";
    public static final String ARGS_CONTENTNAME = "contentName";

    private String SCREEN_NAME;

    private Integer contentId;
    private String contentName;

    private TextView title;
    private RelativeLayout webviewContainerRelativeLayout;
    private WebView contentWebView;
    private ProgressBar progressBar;

    private String webViewContent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        contentId = getArguments().getInt("contentId");
        SCREEN_NAME = getArguments().getString("contentName");

        Log.d(Constants.TAG, SCREEN_NAME);

        if(contentId == 0){
            throw new NullPointerException();
        }


        ((HomeActivity)getActivity()).loadContent(contentId, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);


        webviewContainerRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.webviewContainerRelativeLayout);
        contentWebView = (WebView) rootView.findViewById(R.id.contentWebView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(webViewContent != null){
            contentWebView.loadDataWithBaseURL(null, webViewContent, "text/html", "UTF-8", null);
            progressBar.setVisibility(View.GONE);
            contentWebView.setVisibility(View.VISIBLE);
        }

        MidipileUtilities.setInsetsWithNoTab(getActivity(), webviewContainerRelativeLayout);
    }

    @Override
    public void onDataLoaded() {
        Log.d(Constants.TAG, "onDataLoadedContent");
        String content = ((HomeActivity)getActivity()).getContent(contentId);

        webViewContent = MidipileUtilities.formatWebViewContent(getActivity(), content);

        if(contentWebView != null){
            contentWebView.loadDataWithBaseURL(null, webViewContent, "text/html", "UTF-8", null);
            progressBar.setVisibility(View.GONE);
            contentWebView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Load new content 
     * @param contentId
     * @param contentName
     */
    public void setNewContent(Integer contentId, String contentName){
        progressBar.setVisibility(View.VISIBLE);
        contentWebView.setVisibility(View.GONE);

        this.contentId = contentId;
        this.SCREEN_NAME = contentName;

        ((HomeActivity)getActivity()).loadContent(contentId, this);
    }
}
