package ac.ict.humanmotion.abracadabra.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ac.ict.humanmotion.abracadabra.LoginActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, LoginActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }

}