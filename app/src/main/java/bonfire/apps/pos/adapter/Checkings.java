package bonfire.apps.pos.adapter;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Checkings extends Activity {

    public boolean isInternetOn(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null)
            networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
