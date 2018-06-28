package eazi.com.eazi.configure;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import eazi.com.eazi.R;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;

public class MyService extends Service {

    public static ConnectivityManager cm;
    public static MyXMPP xmpp;


    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<MyService>(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        xmpp = MyXMPP.getInstance(MyService.this, getString(R.string.server),
                CommonMethods.getSharedPrefValue(this, Constants.user_name),
                CommonMethods.getSharedPrefValue(this, Constants.user_pass));
        System.out.println("ChatConfig MyService12345 onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        System.out.println("ChatConfig MyService12345 onStartCommand");

        return Service.START_NOT_STICKY;

    }

    @Override
    public boolean onUnbind(final Intent intent) {
        System.out.println("ChatConfig MyService12345 onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        System.out.println("ChatConfig MyService12345 onDestroy");
        super.onDestroy();
        xmpp.connection.disconnect();
        MyXMPP.instance = null;
    }
}