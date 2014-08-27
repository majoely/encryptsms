package nz.co.joelcompton.encryptsms;

import nz.co.joelcompton.encryptsms.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Send extends Activity {

	public static String infClass = "Send";

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
				
			} else {
				Toast.makeText(getApplicationContext(), "Not good message!", Toast.LENGTH_SHORT).show();
				Log.i(infClass, "message length == 0 or number != match " + number);
			}
		} else {
			Toast.makeText(getApplicationContext(), "Nothing there!", Toast.LENGTH_SHORT).show();
			Log.i(infClass, "phoneField == null or messagefield == null");
		}
		
		
		
		
	}
	
	
}
