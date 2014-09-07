package nz.co.joelcompton.encryptsms;

import java.security.interfaces.ECPublicKey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * Activity to show the unread message.
 * 
 * @author Joel Compton
 *
 */
public class Read extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);
		
		KeyHandler kh = new KeyHandler(getFileStreamPath("private.key"), getFileStreamPath("public.key"));
		
		
		Bundle b = getIntent().getExtras();
		String number = b.getString("PHONE");
		//String message = kh.decryptMessage(b.getString("MESSAGE"), );
		String message = "PLACEHOLDER";
		
		TextView num = (TextView) findViewById(R.id.sms_recieve_from);
		num.setText(number);
		TextView mes =(TextView) findViewById(R.id.sms_recieve_text);
		mes.setText(message);
	}
	
	public void burnMessage(View v) {
		Intent inte = new Intent(this, Reciever.class);
		startActivity(inte);
		finish();
	}
}
