package fr.creads.midipile;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

/**
 * Author : Hugo Gresse
 * Date : 28/08/14
 */
public class MidipileApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        File cacheDir = new File(Environment.getExternalStorageDirectory(), "Midipile/Cache");
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.white)
                .showImageForEmptyUri(android.R.color.white)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
