package nz.co.joelcompton.encryptsms;

import nz.co.joelcompton.encryptsms.R;
import android.app.Activity;
import android.content.Intent;
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
				Buddies b = new Buddies();
				String crypto = b.encryptMessage(number, message);
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(number, null, crypto, null, null);
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
	
	
}
