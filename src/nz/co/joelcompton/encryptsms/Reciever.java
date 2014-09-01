package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
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

public class Reciever extends Activity implements OnClickListener {
	
	public static String infClass = "Recieved";
	private static final String fname = "messages.csv";
	private int sButton;
	private ArrayList<MessageRecieved> messages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView sc = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
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
		sButton = sendButton.getId();
		sendButton.setOnClickListener(this);
		r1.addView(sendButton, b);
		
		RelativeLayout r2 = new RelativeLayout(this);
		Button recieveButton = new Button(this);
		recieveButton.setText("Recieve");
		r2.addView(recieveButton, b);
		
		buttonsLayout.addView(r1, p);
		buttonsLayout.addView(r2, p);
		
		try {
			File f = new File(fname);
			Scanner in = new Scanner(f);
			KeyHandler kh = new KeyHandler();
			this.messages = new ArrayList<>();
			while (in.hasNext()) {
				String[] temp = in.nextLine().split(",");
				MessageRecieved mr = new MessageRecieved(temp[0], kh.decryptMessage(temp[1]));
				this.messages.add(mr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (this.messages != null) {  
			for (int i = 0; i < messages.size(); i++) {
				TextView tv = new TextView(this);
				tv.setText(messages.get(i).getPhoneNumber());
				tv.setTag(i);
				tv.setHeight(150);
				tv.setGravity(Gravity.CENTER);
				tv.setOnClickListener(this);
				ll.addView(tv);
				
	//			Button but = new Button(this);
	//			but.setText("This is a button");
	//			but.setHeight(250);
	//			ll.addView(but);
			}
		}
		
		this.setContentView(sc);
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == sButton) {
			//send them to the send view
		} else {
			//open send activity with v.getId();
			int id = v.getId();
			MessageRecieved m = messages.get(id);
			Log.i(infClass, "SHould open " + id);
		}
		
		
	}
}
