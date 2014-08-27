package nz.co.joelcompton.encryptsms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Buddies {
	
	private static String filePath = "buddies.txt";
	private HashMap<String, String> buddies;
	
	
	public Buddies() {
		File f = new File(filePath);
		if (f.exists()) {
			try {
				Scanner in = new Scanner(f);
				String[] temp = new String[2];
				while (in.hasNext()) {
					temp = in.nextLine().split(":");
					buddies.put(temp[0], temp[1]);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// todo	
	}

	public String encryptMessage(String buddy, String message) {
		String key = buddies.get(buddy);
		
		return "Enrypt " + key + ":" + message;
	}
	
	public void newBuddy(String buddy, String key) {
		buddies.put(buddy, key);
		File f = new File(filePath);
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f));
			out.append(buddy + ":" + key + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
