package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
/**
 * 
 * Activity class to get the phone number from the users so they can be registered on the server.
 * 
 * @author Joel Compton
 *
 */
public class PhoneNumber extends Activity {
	
	public static final String fname = "number.txt";
	private static final String infClass = "encryptsms";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File f = getFileStreamPath(fname);
		if (f.exists()) {
			Intent inte = new Intent(this, Reciever.class);
			startActivity(inte);
			finish();
		}
		setContentView(R.layout.activity_phone_number);
		
	}
	
	public void registerButton(View view) {
		TextView tv = (TextView) findViewById(R.id.phoneNumber);
		String n = tv.getText().toString();
		if (n.length() > 0 && n.matches("02[1257][0-9]{6,7}")) {
			new Register().execute(n);
			File f = getFileStreamPath(fname);
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent inte = new Intent(this, Reciever.class);
			startActivity(inte);
			finish();
		}
	}
	
	private class Register extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			try {
				String token = API.requestToken();
				//String publicKey = API.getPublicKey(number);
				//API.register(token, publicKey, number, 42);
				Log.i(infClass, "number " + params[0]);
				Log.i(infClass, "token  " + token); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(infClass, "Problem on register");
			}
			return null;
		}
		
	}
	
//	protected void onStop(String number) {
//		super.onStop();
//		try {
//			String token = API.requestToken();
//			//String publicKey = API.getPublicKey(number);
//			//API.register(token, publicKey, number, 42);
//			Log.i(infClass, "number " + number);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.i(infClass, "Problem on destroy");
//		}
//	}
}
