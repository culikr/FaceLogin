package culik.com.facelogin;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by LUIZ on 10/06/2016.
 */
public class FaceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

}
