package com.example.projetmobile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;

public class ConnectivityReceiver extends BroadcastReceiver {

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
        void onAirplaneModeChanged(boolean isAirplaneModeOn);
    }

    private ConnectivityReceiverListener listener;

    public ConnectivityReceiver(ConnectivityReceiverListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                boolean isConnected = isNetworkAvailable(context);
                listener.onNetworkConnectionChanged(isConnected);
            } else if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                boolean isAirplaneModeOn = isAirplaneModeOn(context);
                listener.onAirplaneModeChanged(isAirplaneModeOn);
            }
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }
}
