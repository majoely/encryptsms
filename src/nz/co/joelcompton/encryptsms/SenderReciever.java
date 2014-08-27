package nz.co.joelcompton.encryptsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SenderReciever extends BroadcastReceiver {
	
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String infClass = "SenderReciever";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED)) {
			Toast.makeText(context, "Recieved", Toast.LENGTH_SHORT).show();
			Log.i(infClass, "Received message");
			Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
				Object[] pdus = (Object[]) bundle.get("pdus");
			    for (Object pdu: pdus)
			    {
					SmsMessage msg = SmsMessage.createFromPdu((byte[])pdu);
					
					String sender = msg.getOriginatingAddress();
					String message = msg.getMessageBody();
					    
					if (false) { // check if sender is already on the buddies list
						// Will decrypt and store in the sqlite db
						Log.i(infClass, "Is a buddy");
						abortBroadcast();
					} else if (message.startsWith("NEWDHKEY:")) {
						// Recieveing a new key because they didn't have us on their buddy list
						// Reply with our public key
						Log.i(infClass, "Is a new buddy");
						abortBroadcast();
					} else {
						// We should not do any processing on this case.
						Log.i(infClass, "Not in buddies list or new");
					}
					Log.i(infClass, "Sender: " + sender);
					Log.i(infClass, "Message: " + message);
			    }
            }
		}
	}

	public void sendNewMessage(String to, String message) {
		
	}
	
	public void sendPublicKey(String to) {
		KeyHandler kh = new KeyHandler();
		
	}
}
