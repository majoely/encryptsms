package nz.co.joelcompton.encryptsms;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;

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
		/*String formattedNumber = number.replaceFirst("+64", "0");
		String pkey = "";
		String message = "There was an issue decrypting the message";
		try {
			pkey = API.getPublicKey(formattedNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!pkey.equals(""))
		{
			try {
				ECPublicKey ePub = PhoneNumber.kh.generateBuddiesPublicKeyFromString(pkey);
				message = kh.decryptMessage(b.getString("MESSAGE").replaceFirst("ESMS", ""), ePub);
			} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		//String message = "PLACEHOLDER";
		String message = b.getString("MESSAGE").replaceFirst("ESMS", "");
		
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
