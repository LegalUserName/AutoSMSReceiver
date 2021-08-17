package autosmsreceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;


public class AutoSMSReceiver extends BroadcastReceiver {

    public String LogTag = "AutoSMSReceiver";

    public interface OnReceiverSMSP{ void onReceiverSMSP(String code);}
    static public OnReceiverSMSP onReceiverSMSP;
    static public void Register_onReceiverSMSP(OnReceiverSMSP onReceiverSMSP){ AutoSMSReceiver.onReceiverSMSP = onReceiverSMSP; }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LogTag,String.valueOf(intent.getAction()));

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    String[] words = message.split(" ");
                    try {
                        Log.v(LogTag, "Receive:SUCCESS,Digits:" + words[2]);
                        onReceiverSMSP.onReceiverSMSP(words[2]);
                    } catch (NullPointerException e) {
                        Log.v(LogTag, e.getMessage().toString());
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    break;
            }
        }
    }
}
