package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 
 * Class has the list of unread messages which can be read.
 * 
 * @author Joel Compton
 *
 */
public class Reciever extends Activity implements OnClickListener {
	
	public static String infClass = "encryptsms";
	private static final String fname = "/messages.csv";
	private int sButton = 42;
	private ArrayList<MessageRecieved> messages;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScrollView sc = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setPadding(48, 30, 48, 0);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		LinearLayout newLay = new LinearLayout(this);
		newLay.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		sc.addView(ll);
		ll.addView(buttonsLayout);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
		LinearLayout.LayoutParams b = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
		RelativeLayout r1 = new RelativeLayout(this);
		Button sendButton = new Button(this);
		sendButton.setText("Send");
		sendButton.setTag(42);
		sendButton.setOnClickListener(this);
		r1.addView(sendButton, b);
		
		RelativeLayout r2 = new RelativeLayout(this);
		Button recieveButton = new Button(this);
		recieveButton.setText("Recieve");
		r2.addView(recieveButton, b);
		
		buttonsLayout.addView(r1, p);
		buttonsLayout.addView(r2, p);
		
		//this.onResume();
		
//		try {
//			Log.i(infClass, getApplicationContext().getFilesDir().getPath().toString() + fname);
//			File f = new File(getApplicationContext().getFilesDir().getPath().toString() + fname);
//			Scanner in = new Scanner(f);
//			KeyHandler kh = new KeyHandler();
//			this.messages = new ArrayList<>();
//			while (in.hasNext()) {
//				String[] temp = in.nextLine().split(",");
//				MessageRecieved mr = new MessageRecieved(temp[0], kh.decryptMessage(temp[1]));
//				this.messages.add(mr);
//			}
//			f.delete();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if (this.messages != null) {  
//			for (int i = 0; i < messages.size(); i++) {
//				TextView tv = new TextView(this);
//				tv.setText(messages.get(i).getPhoneNumber());
//				tv.setTag(i);
//				tv.setHeight(150);
//				tv.setGravity(Gravity.CENTER);
//				tv.setOnClickListener(this);
//				ll.addView(tv);
//				
//	//			Button but = new Button(this);
//	//			but.setText("This is a button");
//	//			but.setHeight(250);
//	//			ll.addView(but);
//			}
//		}
		
		this.setContentView(sc);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(infClass, "onstart");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(infClass, "onrestart");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(infClass, "onResume");
		LinearLayout newLay = new LinearLayout(this);
		newLay.setOrientation(LinearLayout.VERTICAL);
		try {
			File f = new File(getApplicationContext().getFilesDir().getPath().toString() + fname);
			Scanner in = new Scanner(f);
			KeyHandler kh = PhoneNumber.kh;
			this.messages = new ArrayList<>();
			while (in.hasNext()) {
				String[] temp = in.nextLine().split(",");
				MessageRecieved mr = new MessageRecieved(temp[0], temp[1]);
				this.messages.add(mr);
			}
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(infClass, "exception in reciever onresume");
		}
		
		if (this.messages != null) {  
			for (int i = 0; i < messages.size(); i++) {
				Log.i(infClass, "nextline: " + messages.get(i).getPhoneNumber() );
				TextView tv = new TextView(this);
				tv.setText(messages.get(i).getPhoneNumber());
				tv.setTag(i);
				tv.setHeight(150);
				tv.setGravity(Gravity.CENTER);
				tv.setOnClickListener(this);
				newLay.addView(tv);
				
	//			Button but = new Button(this);
	//			but.setText("This is a button");
	//			but.setHeight(250);
	//			ll.addView(but);
			}
		}
		ll.addView(newLay);
		ll.refreshDrawableState();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.i(infClass, "onstop");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(infClass, "onPause");
		File f = new File(getApplicationContext().getFilesDir().getPath().toString() + fname);
		try {
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			for (int i = 0; i < messages.size(); i++) {
				String message = messages.get(i).getMessage();
				String number = messages.get(i).getPhoneNumber();
				fw.append(number + "," + message + "\n");
				
			}
		fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(infClass, "error onpause");
		}
	}

	@Override
	public void onClick(View v) {
		if ((int) v.getTag() == sButton) {
			//send them to the send view
			Log.i(infClass, "onclick");
			Log.i(infClass, "id:" + v.getId());
			Intent inte = new Intent(this, Send.class);
			startActivity(inte);
			finish();
		} else {
			//open send activity with v.getId();
			int id = (int) v.getTag();
			MessageRecieved m = messages.get(id);
			Log.i(infClass, "Should open " + id);
			this.messages.remove(id);
			Intent inte = new Intent(this, Read.class);
			inte.putExtra("PHONE", m.getPhoneNumber());
			inte.putExtra("MESSAGE", m.getMessage());
			startActivity(inte);
			finish();
		}
	}
}
