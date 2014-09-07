package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.io.IOException;
import java.security.interfaces.ECPublicKey;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//import nz.co.joelcompton.encryptsms.PhoneNumber.Register;

public class Send extends Activity {

	public static String infClass = "encryptsms";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
	}
	
	public void sendMessage(View view) {
		EditText phoneField = (EditText) findViewById(R.id.sms_number);
		EditText messageField = (EditText) findViewById(R.id.sms_text);
		
		if (phoneField != null && messageField != null) {
			String number = phoneField.getText().toString();
			String message = messageField.getText().toString();
			
			if (message.length() > 0 && number.matches("02[1257][0-9]{6,7}")){
				Toast.makeText(getApplicationContext(), "Message Good to go!", Toast.LENGTH_SHORT).show();
//				Buddies b = new Buddies(getApplicationContext());
//				String crypto = b.encryptMessage(number, message);
				//new SendMessage().execute(number, message);
				
				try {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					Toast.makeText(getApplicationContext(), number, Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
					
					String pkey = "";
					try {
						pkey = API.getPublicKey(number);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
					if(pkey.equals(""))
					{
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage(number, null, "This number is trying to communicate securely, please download the Burn After Reading app.", null, null);
					}
					else
					{
						Toast.makeText(getApplicationContext(), pkey, Toast.LENGTH_SHORT).show();
						KeyHandler kh = new KeyHandler(getFileStreamPath("private.key"), getFileStreamPath("public.key"));
						ECPublicKey ePub = kh.generateBuddiesPublicKeyFromString(pkey);
						String crypto = kh.encryptMessage(message, ePub);
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage(number, null, "ESMS" + crypto, null, null);
						Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
						
						Buddies b = new Buddies(getApplicationContext());
						if (!b.isBuddy(number)) {
							b.storeNewKey(number, pkey);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(infClass, "Sending message not worky");
					Toast.makeText(getApplicationContext(), "Message not sent", Toast.LENGTH_SHORT).show();
				}
				
				recieveScreen(view); 
			} else {
				Toast.makeText(getApplicationContext(), "Not good message!", Toast.LENGTH_SHORT).show();
				Log.i(infClass, "message length == 0 or number != match " + number);
			}
		} else {
			Toast.makeText(getApplicationContext(), "Nothing there!", Toast.LENGTH_SHORT).show();
			Log.i(infClass, "phoneField == null or messagefield == null");
		}
		
	}
	
	public void recieveScreen(View v) {
		Intent inte = new Intent(this, Reciever.class);
		startActivity(inte);
		finish();
	}
	
    private class SendMessage extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			
			/*Buddies b = new Buddies(getApplicationContext());
			String crypto = "";
			if (b.isBuddy(params[0])) {
				crypto = b.encryptMessage(params[0], params[1]);
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(params[0], null, "ESMS" + crypto, null, null);
				Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
			} else {
				try {
					String pkey = API.getPublicKey(params[0]);
					ECPublicKey ePub = PhoneNumber.kh.generateBuddiesPublicKeyFromString(pkey);
					//b.storeNewKey(params[0], new BigInteger(pkey));
					//crypto = b.encryptMessage(params[0], params[1]);
					crypto = PhoneNumber.kh.encryptMessage(params[1], ePub);
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(params[0], null, "ESMS" + crypto, null, null);
					Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(infClass, "Sending message not worky");
					Toast.makeText(getApplicationContext(), "Message not sent", Toast.LENGTH_SHORT).show();
				}
			}
			*/
			
			try {
				Toast.makeText(getApplicationContext(), params[0], Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(), params[1], Toast.LENGTH_SHORT).show();
			String pkey = API.getPublicKey(params[0]);
			Toast.makeText(getApplicationContext(), pkey, Toast.LENGTH_SHORT).show();
			ECPublicKey ePub = PhoneNumber.kh.generateBuddiesPublicKeyFromString(pkey);
			//b.storeNewKey(params[0], new BigInteger(pkey));
			//crypto = b.encryptMessage(params[0], params[1]);
			String crypto = PhoneNumber.kh.encryptMessage(params[1], ePub);
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(params[0], null, "ESMS" + crypto, null, null);
			Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(infClass, "Sending message not worky");
				Toast.makeText(getApplicationContext(), "Message not sent", Toast.LENGTH_SHORT).show();
			}
			
			return null;
		}
    	
    }
	
}
