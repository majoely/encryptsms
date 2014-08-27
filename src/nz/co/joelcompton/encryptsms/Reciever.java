package nz.co.joelcompton.encryptsms;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Reciever extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView sc = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sc.addView(ll);
		
		
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
