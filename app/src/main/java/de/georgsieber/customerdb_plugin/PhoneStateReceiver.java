package de.georgsieber.customerdb_plugin;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)
                    || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                if(incomingNumber != null) {
                    final Intent i = new Intent();
                    i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    i.setAction("de.georgsieber.customerdb_plugin.calling");
                    i.putExtra("number", incomingNumber);
                    i.setComponent(new ComponentName("de.georgsieber.customerdb", "de.georgsieber.customerdb.PhoneStateReceiver2"));
                    context.sendBroadcast(i);

                    /*
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.app_name) + ":\n" + incomingNumber,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }, 500);
                    */
                }
            }

            /*
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.d("cutomerdbphonedbg", "RINGING::"+incomingNumber);
            }
            if((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                Log.d("cutomerdbphonedbg", "OFFHOOK::"+incomingNumber);
            }
            if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d("cutomerdbphonedbg", "IDLE::"+incomingNumber);
            }
            */
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("cutomerdbphonedbg", e.getMessage());
        }
    }
}
