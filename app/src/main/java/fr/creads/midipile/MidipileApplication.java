package fr.creads.midipile;

import android.app.Application;
import android.os.Environment;

import com.facebook.SessionDefaultAudience;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;

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

        // set log to true
        Logger.DEBUG_WITH_STACKTRACE = true;
        // initialize facebook configuration
        Permission[] permissions = new Permission[] {
                Permission.PUBLIC_PROFILE
        };
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(getString(R.string.app_id))
                .setNamespace("midipile")
                .setPermissions(permissions)
                .setDefaultAudience(SessionDefaultAudience.FRIENDS)
                .setAskForAllPermissionsAtOnce(true)
                .build();
        SimpleFacebook.setConfiguration(configuration);
    }
}
