package nz.co.joelcompton.encryptsms;

import java.math.BigInteger;

import nz.co.joelcompton.encryptsms.R;
import nz.co.joelcompton.encryptsms.PhoneNumber.Register;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
				new SendMessage().execute(number, message);
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
			
			Buddies b = new Buddies(getApplicationContext());
			String crypto = "";
			if (b.isBuddy(params[0])) {
				crypto = b.encryptMessage(params[0], params[1]); 
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(params[0], null, "ESMS" + crypto, null, null);
				Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
			} else {
				try {
					String pkey = API.getPublicKey(params[0]);
					b.storeNewKey(params[0], new BigInteger(pkey));
					crypto = b.encryptMessage(params[0], params[1]); 
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(params[0], null, "ESMS" + crypto, null, null);
					Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(infClass, "Sending message not worky");
					Toast.makeText(getApplicationContext(), "Message not sent", Toast.LENGTH_SHORT).show();
				}
			}
			
			return null;
		}
    	
    }
	
}
