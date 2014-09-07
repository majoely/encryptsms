package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
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
	private static final String infClass = "encryptsms";
	private static final String fname = "/messages.csv";

	@SuppressWarnings("unused")
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
					Buddies b = new Buddies(context);
					String sender = msg.getOriginatingAddress();
					String message = msg.getMessageBody();
										    
					if (message.startsWith("ESMS")) { 
						Log.i(infClass, "Is a buddy");
						String number = sender;
						
						try {
							String pkey = API.getPublicKey(number);
							ECPublicKey ePub = PhoneNumber.kh.generateBuddiesPublicKeyFromString(pkey);
							String decrypto = PhoneNumber.kh.decryptMessage(message.replaceFirst("ESMS", ""), ePub);
							
							Toast.makeText(context, "Recieved From: " + number + " Message: " + decrypto, Toast.LENGTH_LONG).show();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvalidKeySpecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
//						if (!b.isBuddy(sender)) {
//							try {
//								API.getPublicKey(sender);
//							} catch (Exception e) {
//								Log.i(infClass, "Can't get buddies public key");
//							}
//						}
						abortBroadcast();
						File f = new File(context.getFilesDir().getPath().toString() + fname);
						if(!f.exists()) {
							try {
								f.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.i(infClass, "Exception in senderreciever");
							}
						}
						try {
							FileWriter fw = new FileWriter(f);
							fw.append(sender + "," + message.substring(4) + "\n");
							fw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
//					} else if (message.startsWith("NEWDHKEY:")) {
//						// Recieveing a new key because they didn't have us on their buddy list
//						// Reply with our public key
//						Log.i(infClass, "Is a new buddy");
//						abortBroadcast();
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


}
