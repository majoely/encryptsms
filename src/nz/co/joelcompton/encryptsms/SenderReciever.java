package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
	private static final String fname = "messages.csv";

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
					//Buddies b = new Buddies();
					String sender = msg.getOriginatingAddress();
					String message = msg.getMessageBody();
										    
					if (false) { 
						Log.i(infClass, "Is a buddy");
						abortBroadcast();
						File f = new File(fname);
						if(!f.exists()) {
							try {
								f.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							FileWriter fw = new FileWriter(f);
							fw.append(sender + "," + message + "\n");
						} catch (Exception e) {
							e.printStackTrace();
						}
						
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
