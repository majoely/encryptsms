package nz.co.joelcompton.encryptsms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Reciever extends Activity {
	
	public static String infClass = "Recieved";

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
		RelativeLayout r1 = new RelativeLayout(this);
		Button sendButton = new Button(this);
		sendButton.setText("Send");
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i(infClass, "Would know change to Send view");
				
			}
		});
		r1.addView(sendButton);
		
		RelativeLayout r2 = new RelativeLayout(this);
		Button recieveButton = new Button(this);
		recieveButton.setText("Recieve");
		r2.addView(recieveButton);
		
		buttonsLayout.addView(r1, p);
		buttonsLayout.addView(r2, p);
		
		for (int i = 0; i < 20; i++) {
//			TextView tv = new TextView(this);
//			tv.setText("This is some text");
//			tv.setHeight(150);
//			ll.addView(tv);
			Button but = new Button(this);
			but.setText("This is a button");
			but.setHeight(250);
			ll.addView(but);
		}
		
		this.setContentView(sc);
		
	}
}
